package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.service.MovieService;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PlexMovieWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(PlexMovieWriter.class.getName());

    @Inject private StandaloneMovieWriter movieWriter;
    @Inject private SerieItemWriter serieItemWriter;
    @Inject private MovieService service;
    @Inject private String path;

    public void process(final List<String> list) {
        list.stream()
                .map(fileName -> Paths.get(fileName).toFile().getAbsolutePath())
                .map(fileName -> service.findByFilename(fileName.substring(path.length())))
                .filter(movie -> null != movie)
                .forEach(movie -> {
                    Serie serie = movie.getSerie();

                    if (null != serie && serie.getTvShow()) {
                        try {
                            serieItemWriter.process(movie);
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, "IO/Error creating a link for serie item " + movie.getFileName(), ex);
                        }
                    } else {
                        try {
                            movieWriter.process(movie);
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, "IO/Error creating a link for movie " + movie.getFileName(), ex);
                        }
                    }
                });
        
        LOG.log(Level.INFO, "Processed {0} movie/tv-series items", list.size());
    }
}
