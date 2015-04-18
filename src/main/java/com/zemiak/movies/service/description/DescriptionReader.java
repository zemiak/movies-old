package com.zemiak.movies.service.description;

import com.zemiak.movies.domain.Movie;
import java.util.Arrays;

public class DescriptionReader {
    private static final int SERIES_DESCRIPTIONS_INDEX = 0;

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

    public boolean canFetchDescription(final Movie movie) {
        return Arrays.asList(readers).stream()
                .filter(reader -> reader.accepts(movie))
                .findFirst()
                .isPresent();
    }

    public boolean isSpecialDescriptions(Movie movie) {
        return readers[SERIES_DESCRIPTIONS_INDEX].accepts(movie);
    }
}
