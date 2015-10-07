package com.zemiak.movies.batch.movies;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.service.MovieService;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class NewMoviesCreator {
    @Inject private MovieService service;
    @Inject private String path;

    private static final BatchLogger LOG = BatchLogger.getLogger(NewMoviesCreator.class.getName());

    public void process(final List<String> files) {
        files.stream()
                .map(this::getRelativeFilename)
                .filter(fileName -> null == service.findByFilename(fileName))
                .map(fileName -> service.create(fileName))
                .forEach(m -> {
                    LOG.log(Level.INFO, "Created a new movie ''{0}''/''{1}'', id {2}...",
                            new Object[]{m.getFileName(), m.getName(), m.getId()});
                });
    }

    private String getRelativeFilename(final String absoluteFilename) {
        String absoluteWithSlashes = absoluteFilename;
        if (absoluteWithSlashes.startsWith(path)) {
            absoluteWithSlashes = absoluteWithSlashes.substring(path.length());
        }

        return absoluteWithSlashes;
    }
}
