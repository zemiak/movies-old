package com.zemiak.movies.ui.view.movie.list;

/**
 *
 * @author vasko
 */
public class MovieListRefreshEvent {
    private boolean onlyNew;
    
    public MovieListRefreshEvent() {
        this(false);
    }
    
    public MovieListRefreshEvent(boolean onlyNew) {
        this.onlyNew = onlyNew;
    }

    public boolean isOnlyNew() {
        return onlyNew;
    }
}
