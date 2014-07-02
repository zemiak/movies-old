package com.zemiak.movies.admin.jsf.movie;

import com.zemiak.movies.admin.beans.MovieFacade;
import com.zemiak.movies.domain.Movie;

/**
 *
 * @author vasko
 */
public interface ChangeMovieItem {
    void change(final Movie movie);
}
