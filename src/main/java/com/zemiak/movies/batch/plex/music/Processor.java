package com.zemiak.movies.batch.plex.music;

import com.zemiak.movies.service.MovieService;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PlexMusicProcessor")
public class Processor implements ItemProcessor {
    @Inject private MovieService service;
    @Inject private String path;
    @Inject private JobContext jobCtx;

    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        return service.findByFilename(fileName.substring(path.length()));
    }
}
