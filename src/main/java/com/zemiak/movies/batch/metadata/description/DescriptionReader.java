package com.zemiak.movies.batch.metadata.description;

import com.zemiak.movies.domain.Movie;

public class DescriptionReader {
    private final Csfd csfd = new Csfd();
    private final Imdb imdb = new Imdb();
    private final Mash mash = new Mash();
    
    public DescriptionReader() {
    }
    
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
