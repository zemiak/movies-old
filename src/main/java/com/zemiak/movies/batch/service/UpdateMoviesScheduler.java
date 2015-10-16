package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.movies.DescriptionsUpdater;
import com.zemiak.movies.batch.movies.MetadataRefresher;
import com.zemiak.movies.batch.movies.NewMoviesCreator;
import com.zemiak.movies.batch.movies.ThumbnailCreator;
import com.zemiak.movies.batch.plex.*;
import com.zemiak.movies.domain.CacheClearEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UpdateMoviesScheduler {
    private static final Logger LOG = Logger.getLogger(UpdateMoviesScheduler.class.getName());

    @Inject private String developmentSystem;
    @Inject private PrepareMovieFileList movieFileList;
    @Inject private PrepareMusicFileList musicFileList;
    @Inject private NewMoviesCreator creator;
    @Inject private MetadataRefresher refresher;
    @Inject private DescriptionsUpdater descUpdater;
    @Inject private ThumbnailCreator thumbnails;
    @Inject private BasicPlexFolderStructureCreator plexFolders;
    @Inject private PlexMovieWriter plexMovies;
    @Inject private PlexMusicWriter plexMusic;
    @Inject private SendLogFile logFileMailer;

    @Inject
    private javax.enterprise.event.Event<CacheClearEvent> clearEvent;

    @Schedule(dayOfWeek="*", hour="03", minute="10")
    public void startScheduled() {
        if ("true".equals(developmentSystem)) {
            LOG.log(Level.SEVERE, "Scheduled batch update cancelled, a development system is in use.");
            return;
        }

        start();
    }

    public void start() {
        BatchLogger.deleteLogFile();

        creator.process(movieFileList.getFiles());
        refresher.process(movieFileList.getFiles());
        descUpdater.process(movieFileList.getFiles());
        thumbnails.process(movieFileList.getFiles());

        plexFolders.cleanAndCreate();
        plexMovies.process(movieFileList.getFiles());
        plexMusic.process(musicFileList.getFiles());

        logFileMailer.send();
        clearEvent.fire(new CacheClearEvent());
    }
}
