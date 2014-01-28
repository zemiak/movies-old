package com.zemiak.movies.batch.metadata.description;

import com.zemiak.movies.domain.Movie;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class DescriptionReader {
    @Inject private Csfd csfd;
    @Inject private Imdb imdb;
    @Inject private Mash mash;
    
    public String read(final Movie movie) {
        if (csfd.accepts(movie)) {
            return csfd.getDescription(movie);
        }
        
        if (imdb.accepts(movie)) {
            return imdb.getDescription(movie);
        }
        
        if (mash.accepts(movie)) {
            return mash.getDescription(movie);
        }
        
        return null;
    }
}
