package com.zemiak.movies.batch.movies;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.description.DescriptionReader;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class DescriptionsUpdater {
    private static final BatchLogger LOG = BatchLogger.getLogger(DescriptionsUpdater.class.getName());

    private final DescriptionReader descriptions = new DescriptionReader();
    @Inject private MovieService service;
    @Inject private String path;

    public void process(final List<String> files) {
        files.stream()
                .map(fileName -> service.findByFilename(fileName.substring(path.length())))
                .filter(movie -> null != movie && movie.isDescriptionEmpty() && descriptions.canFetchDescription(movie))
                .forEach(movie -> {
                    String desc = descriptions.read(movie);

                    movie.setDescription(desc);
                    service.mergeAndSave(movie);

                    LOG.log(Level.INFO, "... update description in DB of ", movie.getFileName());
                });
    }
}
