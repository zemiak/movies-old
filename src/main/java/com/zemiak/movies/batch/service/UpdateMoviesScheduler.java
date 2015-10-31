package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.movies.DescriptionsUpdater;
import com.zemiak.movies.batch.movies.MetadataRefresher;
import com.zemiak.movies.batch.movies.NewMoviesCreator;
import com.zemiak.movies.batch.movies.ThumbnailCreator;
import com.zemiak.movies.batch.plex.*;
import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.service.BackupService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UpdateMoviesScheduler {
    private static final Logger LOG = Logger.getLogger(UpdateMoviesScheduler.class.getName());

    @Inject Boolean developmentSystem;
    @Inject PrepareMovieFileList movieFileList;
    @Inject PrepareMusicFileList musicFileList;
    @Inject NewMoviesCreator creator;
    @Inject MetadataRefresher refresher;
    @Inject DescriptionsUpdater descUpdater;
    @Inject ThumbnailCreator thumbnails;
    @Inject BasicPlexFolderStructureCreator plexFolders;
    @Inject PlexMovieWriter plexMovies;
    @Inject PlexMusicWriter plexMusic;
    @Inject SendLogFile logFileMailer;
    @Inject TriggerPlexRefresh plexRefresh;
    @Inject RefreshStatistics stats;
    @Inject javax.enterprise.event.Event<CacheClearEvent> clearEvent;
    @Inject BackupService backup;

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
//        if (developmentSystem) {
//            LOG.log(Level.INFO, "Scheduled backup cancelled, a development system is in use.");
//            return;
//        }

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

        plexFolders.cleanAndCreate();
        plexMovies.process(movieFileList.getFiles());
        plexMusic.process(musicFileList.getFiles());
        plexRefresh.refreshAll();
        stats.dump();

        logFileMailer.send();
        clearEvent.fire(new CacheClearEvent());
    }
}
