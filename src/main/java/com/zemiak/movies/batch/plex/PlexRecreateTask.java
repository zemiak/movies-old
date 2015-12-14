package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.plex.movie.PlexMovieWriter;
import com.zemiak.movies.batch.plex.movie.PrepareMovieFileList;
import com.zemiak.movies.batch.plex.music.PlexMusicWriter;
import com.zemiak.movies.batch.plex.music.PrepareMusicFileList;
import com.zemiak.movies.batch.plex.photo.PlexPhotoWriter;
import com.zemiak.movies.batch.plex.photo.PreparePhotoFileList;
import javax.inject.Inject;

public class PlexRecreateTask {
    @Inject PlexMovieWriter plexMovies;
    @Inject PlexMusicWriter plexMusic;
    @Inject PlexPhotoWriter plexPhotos;
    @Inject PrepareMovieFileList movieFileList;
    @Inject PrepareMusicFileList musicFileList;
    @Inject PreparePhotoFileList photoFileList;
    @Inject BasicPlexFolderStructureCreator plexFolders;
    @Inject TriggerPlexRefresh plexLibraryRefresh;
    @Inject PlexProcess process;
    @Inject PlexDatabase db;

    public void execute() {
        try {
            process.stop();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Cannot stop Plex process");
        }


        // create folders and media links
        plexFolders.cleanAndCreate();
        plexMovies.process(movieFileList.getFiles());
        plexMusic.process(musicFileList.getFiles());
        plexPhotos.process(photoFileList.getFiles());

        // may be, that in Plex, new movies are not yet imported, but it'll try to add them into a serie
        db.refresh();

        // refresh collections and media libs
        try {
            process.start();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Cannot start Plex process");
        }

        plexLibraryRefresh.refreshAll();
    }
}
