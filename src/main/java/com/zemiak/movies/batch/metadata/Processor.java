package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

@Named("MetadataProcessor")
public class Processor implements ItemProcessor {
    private static final BatchLogger LOG = BatchLogger.getLogger(Processor.class.getName());

    @Inject private MovieService service;
    @Inject private String path;

    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        Movie movie = service.findByFilename(fileName.substring(path.length()));
        MovieMetadata data = new MetadataReader(fileName, movie, service).get();


        if (null != movie && null != data) {
            if (! data.isMetadataEqual()) {
                LOG.info("Metadata: going to update " + fileName);
                return data;
            }
        }

        return null;
    }
}
