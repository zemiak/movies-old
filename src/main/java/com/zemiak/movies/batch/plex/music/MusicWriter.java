package com.zemiak.movies.batch.plex.music;

import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class MusicWriter {
    static final String PATH = "Music";

    @Inject String path;

    public void process(String plexFolder, Movie movie) throws IOException {
        String movieName = null == movie.getOriginalName() ? movie.getName() : movie.getOriginalName();
        Path linkName = Paths.get(plexFolder, PATH, movieName + ".m4v");

        Integer i = 2;
        while (Files.exists(linkName)) {
            linkName = Paths.get(plexFolder, PATH, movieName +  "-" + String.valueOf(i) + ".m4v");

            if (i > 100) {
                throw new IllegalStateException("Cannot find a suitable name for " + linkName.toString());
            }
        }

        Path existing = Paths.get(path, movie.getFileName());
        Files.createSymbolicLink(linkName, existing);
    }
}
