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

    public void execute() {
        // create folders and media links
        plexFolders.cleanAndCreate();
        plexMovies.process(movieFileList.getFiles());
        plexMusic.process(musicFileList.getFiles());
        plexPhotos.process(photoFileList.getFiles());
    }
}
