package com.zemiak.movies.service.description;

import com.zemiak.movies.domain.Movie;

public class DescriptionReader {
    private final IDescriptionReader[] readers = new IDescriptionReader[]{
        new SeriesDescriptionReader(),
        new Csfd(),
        new Imdb()
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