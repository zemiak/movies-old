package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;

public class DescriptionReader {
    private final IDescriptionReader[] readers = new IDescriptionReader[]{
        new Csfd(), new Imdb(), new Mash()
    };
    
    public DescriptionReader() {
    }
    
    public String read(final Movie movie) {
        for (IDescriptionReader reader: readers) {
            if (reader.accepts(movie)) {
                return reader.getDescription(movie);
            }
        }
        
        return null;
    }
}
