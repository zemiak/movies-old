package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PlexWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Inject private MovieService service;
    @Inject private JobContext jobCtx;

    @Override
    public void writeItems(final List list) throws Exception {
        String plexFolder = jobCtx.getProperties().getProperty("plexFolder");
        list.stream().filter(obj -> null != obj).forEach(obj -> {
            Movie movie = (Movie) obj;
            
        });
    }
}
