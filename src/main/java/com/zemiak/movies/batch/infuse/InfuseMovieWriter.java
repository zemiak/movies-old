package com.zemiak.movies.batch.infuse;

import com.zemiak.movies.batch.service.RefreshStatistics;
import com.zemiak.movies.batch.service.logs.BatchLogger;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
public class InfuseMovieWriter {
    private static final Logger LOG1 = Logger.getLogger(InfuseMovieWriter.class.getName());
    private static final BatchLogger LOG = BatchLogger.getLogger(InfuseMovieWriter.class.getName());

    @Inject MovieService service;
    @Inject String path;
    @Inject String infuseLinkPath;
    @Inject RefreshStatistics stats;
    @Inject InfuseCoversAndLinks metadataFiles;
    @PersistenceContext EntityManager em;

    public void process(final List<String> list) {
        list.stream()
                .map(fileName -> Paths.get(fileName).toFile().getAbsolutePath())
                .map(fileName -> service.findByFilename(fileName.substring(path.length())))
                .filter(movie -> null != movie)
                .forEach(this::makeMovieLinkNoException);

        makeRecentlyAdded();
        makeNewReleases();

        metadataFiles.createGenreAndSerieCovers();
    }

    private void makeMovieLinkNoException(Movie movie) {
        try {
            makeMovieLink(movie);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot make movie link for " + movie.getFileName() + ": " + ex.getMessage(), null);
        }
    }

    private void makeMovieLink(Movie movie) throws IOException {
        if (null == movie.getGenre()) {
            LOG.log(Level.SEVERE, "Movie {0} has no genre", movie.getFileName());
            return;
        }

        String movieName = (null == movie.getOriginalName() || "".equals(movie.getOriginalName().trim()))
                ? movie.getName() : movie.getOriginalName();
        if (null == movieName || "".equals(movieName)) {
            LOG.log(Level.SEVERE, "Movie {0} has no name", movie.getFileName());
            return;
        }

        int i = 0;
        while (!metadataFiles.createLink(movie, movieName, i)) {
            i++;
        }

        stats.incrementLinksCreated();

        LOG.log(Level.FINE, "Created Infuse movie link for movie ", movie.getFileName());
    }

    private void makeRecentlyAdded() {
        Genre genre = Genre.create();
        genre.setId(-1);
        genre.setName("X-Recently Added");

        service.getRecentlyAdded().stream().forEach(movie -> {
            em.detach(movie);
            movie.setGenre(genre);
            movie.setSerie(null);
            makeMovieLinkNoException(movie);
        });
    }

    private void makeNewReleases() {
        Genre genre = Genre.create();
        genre.setId(-2);
        genre.setName("X-New Releases");

        service.getNewReleases().stream().forEach(movie -> {
            em.detach(movie);
            movie.setGenre(genre);
            movie.setSerie(null);
            makeMovieLinkNoException(movie);
        });
    }
}