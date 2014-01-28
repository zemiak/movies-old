package com.zemiak.movies.batch.metadata.description;

import com.zemiak.movies.domain.Movie;

/**
 *
 * @author vasko
 */
public interface IDescriptionReader {
    public boolean accepts(final Movie movie);
    public String getDescription(final Movie movie);
}
