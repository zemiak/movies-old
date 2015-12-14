package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.infuse.InfuseService;
import com.zemiak.movies.batch.movies.*;
import com.zemiak.movies.batch.plex.PlexRecreateTask;
import com.zemiak.movies.batch.plex.PlexService;
import com.zemiak.movies.batch.plex.movie.PrepareMovieFileList;
import com.zemiak.movies.batch.plex.music.PrepareMusicFileList;
import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.service.BackupService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Stateless
public class UpdateMoviesScheduler {
    private static final Logger LOG = Logger.getLogger(UpdateMoviesScheduler.class.getName());

    @Inject Boolean developmentSystem;
    @Inject NewMoviesCreator creator;
    @Inject MetadataRefresher refresher;
    @Inject DescriptionsUpdater descUpdater;
    @Inject ThumbnailCreator thumbnails;
    @Inject PrepareMovieFileList movieFileList;
    @Inject PrepareMusicFileList musicFileList;
    @Inject PlexRecreateTask plexRecreateTask;
    @Inject SendLogFile logFileMailer;
    @Inject RefreshStatistics stats;
    @Inject Event<CacheClearEvent> clearEvent;
    @Inject BackupService backup;
    @Inject WebScrobbler scrobbler;

    @Inject InfuseService infuseService;
    @Inject PlexService plexService;

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

        stats.reset();
        creator.process(movieFileList.getFiles());
        refresher.process(movieFileList.getFiles());
        descUpdater.process(movieFileList.getFiles());
        thumbnails.process(movieFileList.getFiles());
        scrobbler.process(movieFileList.getFiles());

        plexService.process(movieFileList.getFiles(), musicFileList.getFiles());
        infuseService.process(movieFileList.getFiles());

        stats.dump();

        logFileMailer.send();
        clearEvent.fire(new CacheClearEvent());
    }
}
