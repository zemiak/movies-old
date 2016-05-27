package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.infuse.InfuseService;
import com.zemiak.movies.batch.metadata.MetadataService;
import com.zemiak.movies.batch.service.logs.BatchLogger;
import com.zemiak.movies.batch.service.logs.SendLogFile;
import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.service.BackupService;
import com.zemiak.movies.service.tvml.CacheDataReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Stateless
public class UpdateMoviesScheduler {
    private static final Logger LOG = Logger.getLogger(UpdateMoviesScheduler.class.getName());
    private static final BatchLogger LOG1 = BatchLogger.getLogger(UpdateMoviesScheduler.class.getName());

    @Inject Boolean developmentSystem;

    @Inject SendLogFile logFileMailer;
    @Inject RefreshStatistics stats;
    @Inject Event<CacheClearEvent> clearEvent;
    @Inject BackupService backup;
    @Inject InfuseService infuseService;
    @Inject MetadataService metadataService;
    @Inject CacheDataReader cache;

    @Schedule(dayOfWeek="*", hour="03", minute="10")
    public void startScheduled() {
        if (developmentSystem) {
            LOG.log(Level.INFO, "Scheduled batch update cancelled, a development system is in use.");
            return;
        }


        start();
    }

    @Schedule(dayOfWeek="0,2,4,6", hour="05", minute="15")
    public void backupAndClean() {
        if (developmentSystem) {
            LOG.log(Level.INFO, "Scheduled backup cancelled, a development system is in use.");
            return;
        }

        backup.removeOldBackups();
        backup.backup();
    }

    public void start() {
        BatchLogger.deleteLogFile();

        try {
            stats.reset();

            metadataService.process();
            infuseService.process();

            stats.dump();

            if (stats.haveBeenChanged()) {
                cache.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG1.log(Level.SEVERE, "Exception running movies update batch " + ex.getMessage(), ex);
        }

        logFileMailer.send();
        clearEvent.fire(new CacheClearEvent());
    }
}
