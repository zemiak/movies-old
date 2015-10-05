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
            process(folder, movie, 2, movie.getDisplayOrder() % 100);
        } else if (Objects.equals(MASH, serie.getId())) {
            process(folder, movie, 3, 1);
        } else {
            process(folder, movie, 2, 1);
        }
    }

    private void process(Path folder, Movie movie, Integer decimals, Integer season) throws IOException {
        String serie = movie.getSubtitlesName();
        String format = "%0" + String.valueOf(decimals) + "d";
        Integer number = null == movie.getDisplayOrder() ? 0 : movie.getDisplayOrder();
        String episodeName = serie + " - s01e" + String.format(format, number) + " - "
                + movie.getOriginalLanguageName() + ".m4v";

        Path linkName = Paths.get(folder.toString(), serie, String.format("Season %02d", season), episodeName);
        Path existing = Paths.get(path, movie.getFileName());

        Files.createDirectories(Paths.get(folder.toString(), "Season %02d"));
        Files.createSymbolicLink(linkName, existing);
    }
}