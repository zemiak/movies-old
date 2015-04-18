package com.zemiak.movies.batch.descriptions;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.description.DescriptionReader;
import java.util.logging.Level;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

@Named("DescriptionsProcessor")
public class Processor implements ItemProcessor {
    private static final BatchLogger LOG = BatchLogger.getLogger(Processor.class.getName());
    private static final DescriptionReader DESCRIPTIONS = new DescriptionReader();

    @Inject private MovieService service;
    @Inject private String path;

    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        Movie movie = service.findByFilename(fileName.substring(path.length()));

        if (null != movie && movie.isDescriptionEmpty() && DESCRIPTIONS.canFetchDescription(movie)) {
            LOG.log(Level.INFO, "Going to update movie DB description ", movie.getFileName());
            return movie;
        }

        return null;
    }
}
