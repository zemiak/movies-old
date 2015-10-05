package com.zemiak.movies.batch.plex.music;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

@Named("PlexMusicProcessor")
public class Processor implements ItemProcessor {
    @Override
    public Object processItem(final Object musicFileName) throws Exception {
        return musicFileName;
    }
}
