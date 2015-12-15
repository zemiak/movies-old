package com.zemiak.movies.batch.infuse;

import com.zemiak.movies.batch.service.logs.BatchLogger;
import com.zemiak.movies.batch.service.RefreshStatistics;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.strings.Encodings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class InfuseMovieWriter {
    static final String PATH = "Movies";
    private static final BatchLogger LOG = BatchLogger.getLogger(InfuseMovieWriter.class.getName());

    @Inject MovieService service;
    @Inject String path;
    @Inject String infuseLinkPath;
    @Inject RefreshStatistics stats;

    public void process(final List<String> list) {
        list.stream()
                .map(fileName -> Paths.get(fileName).toFile().getAbsolutePath())
                .map(fileName -> service.findByFilename(fileName.substring(path.length())))
                .filter(movie -> null != movie)
                .forEach(this::makeMovieLinkNoException);
    }

    private void makeMovieLinkNoException(Movie movie) {
        try {
            makeMovieLink(movie);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot make movie link for " + movie.getFileName() + ": " + ex.getMessage(), null);
        }
    }

    private void makeMovieLink(Movie movie) throws IOException {
        Genre genre = movie.getGenre();
        if (null == genre) {
            LOG.log(Level.SEVERE, "Movie {0} has no genre", movie.getFileName());
            return;
        }

        String movieName = (null == movie.getOriginalName() || "".equals(movie.getOriginalName().trim()))
                ? movie.getName() : movie.getOriginalName();
        if (null == movieName || "".equals(movieName)) {
            LOG.log(Level.SEVERE, "Movie {0} has no name", movie.getFileName());
            return;
        }

        Serie serie = movie.getSerie();
        Path linkName;
        if (null == serie || serie.isEmpty()) {
            Files.createDirectories(Paths.get(infuseLinkPath, infuseLinkPath, PATH,
                    Encodings.deAccent(genre.getName())
            ));

            linkName = Paths.get(infuseLinkPath, PATH,
                    Encodings.deAccent(genre.getName()),
                    Encodings.deAccent(movieName) + ".m4v");
        } else {
            Files.createDirectories(Paths.get(infuseLinkPath, infuseLinkPath, PATH,
                    Encodings.deAccent(genre.getName()),
                    Encodings.deAccent(serie.getName())
            ));

            linkName = Paths.get(infuseLinkPath, PATH,
                    Encodings.deAccent(genre.getName()),
                    Encodings.deAccent(serie.getName()),
                    service.getNiceDisplayOrder(movie) + " " + Encodings.deAccent(movieName) + ".m4v");
        }

        Path existing = Paths.get(path, movie.getFileName());
        Files.createSymbolicLink(linkName, existing);
        stats.incrementLinksCreated();

        LOG.log(Level.FINE, "Created Infuse movie link {0} -> {1}", new Object[]{linkName.toString(), existing.toString()});
    }
}
