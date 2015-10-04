package com.zemiak.movies.batch.plex;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class SerieItemWriter {
    static final String PATH = "TV Shows";
    static final Integer GOT = 1000;
    static final Integer MASH = 1;

    @Inject String path;

    public void process(String plexFolder, Movie movie) throws IOException {
        Serie serie = movie.getSerie();
        Path folder = Paths.get(path, PATH, serie.getName());

        Files.createDirectories(folder);

        if (Objects.equals(GOT, serie.getId())) {
            processGameOfThrones(folder, movie);
        } else if (Objects.equals(MASH, serie.getId())) {
            processMash(folder, movie);
        } else {
            processStandard(folder, movie);
        }
    }

    private void processStandard(Path folder, Movie movie) throws IOException {
        String serie = movie.getSubtitlesName();
        Integer number = null == movie.getDisplayOrder() ? 0 : movie.getDisplayOrder();
        String episodeName = serie + " - s01e" + String.format("%02d", number) + " - "
                + movie.getOriginalLanguageName() + ".m4v";

        Path linkName = Paths.get(folder.toString(), serie, "Season 01", episodeName);
        Path existing = Paths.get(path, movie.getFileName());
        Files.createSymbolicLink(linkName, existing);
    }

    private void processMash(Path folder, Movie movie) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void processGameOfThrones(Path folder, Movie movie) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
