package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.plex.movie.PlexMovieWriter;
import com.zemiak.movies.batch.plex.music.PlexMusicWriter;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PlexService {
    @Inject BasicPlexFolderStructureCreator plexFolders;
    @Inject PlexMovieWriter plexMovies;
    @Inject PlexMusicWriter plexMusic;
    @Inject TriggerPlexRefresh plexRefresh;

    public void process(List<String> movieFileList, List<String> musicFileList) {
        plexFolders.cleanAndCreate();
        plexMovies.process(movieFileList);
        plexMusic.process(musicFileList);
        plexRefresh.refreshAll();
    }
}
