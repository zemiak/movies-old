package com.zemiak.movies.batch.plex;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class SerieItemWriter {
    private static final Logger LOG = Logger.getLogger(SerieItemWriter.class.getName());
    static final String PATH = "TV Shows";
    static final Integer GOT = 1000;
    static final Integer MASH = 1;

    @Inject String plexPath;
    @Inject String path;

    public void process(Movie movie) throws IOException {
        Serie serie = movie.getSerie();
        Path folder = Paths.get(plexPath, PATH, serie.getName());

        Files.createDirectories(folder);

        if (Objects.equals(GOT, serie.getId())) {
            process(folder, movie, 2, movie.getDisplayOrder() / 100);
        } else if (Objects.equals(MASH, serie.getId())) {
            process(folder, movie, 3, 1);
        } else {
            process(folder, movie, 2, 1);
        }
    }

    private void process(Path folder, Movie movie, Integer decimals, Integer season) throws IOException {
        String serie = StandaloneMovieWriter.deAccent(movie.getSerieName());
        String seasonNumber = String.format("%02d", season);
        String format = "%0" + String.valueOf(decimals) + "d";
        Integer number = null == movie.getDisplayOrder() ? 0 : movie.getDisplayOrder();
        String movieName = (null == movie.getOriginalName() || "".equals(movie.getOriginalName().trim()))
                ? movie.getName() : movie.getOriginalName();

        if (null == movieName || "".equals(movieName)) {
            LOG.log(Level.SEVERE, "TV Show episode {0} has no name", movie.getFileName());
            return;
        }

        String episodeName = serie + " - s" + seasonNumber + "e" + String.format(format, number) + " - "
                + StandaloneMovieWriter.deAccent(movieName) + ".m4v";

        Path linkName = Paths.get(folder.toString(), "Season " + seasonNumber, episodeName);
        Path existing = Paths.get(path, movie.getFileName());

        Files.createDirectories(Paths.get(folder.toString(), String.format("Season %02d", season)));
        Files.createSymbolicLink(linkName, existing);

        LOG.log(Level.FINEST, "Created TV Show link {0} -> {1}", new Object[]{linkName.toString(), existing.toString()});
    }
}
