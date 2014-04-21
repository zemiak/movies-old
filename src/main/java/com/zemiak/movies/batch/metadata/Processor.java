package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.configuration.Configuration;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("MetadataProcessor")
public class Processor implements ItemProcessor {
    private static final BatchLogger LOG = BatchLogger.getLogger(Processor.class.getName());
    
    @Inject MovieService service;
    @Inject Configuration conf;

    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        Movie movie = service.findByFilename(fileName.substring(conf.getPath().length()));
        MovieMetadata data = new MetadataReader(fileName, movie).get();


        if (null != movie && null != data) {
            if (! data.isMetadataEqual()) {
                System.out.println("Metadata: going to update " + fileName);
                return data;
            }
        }

        //System.out.println("Metadata: skipping " + fileName);

        return null;
    }
}
