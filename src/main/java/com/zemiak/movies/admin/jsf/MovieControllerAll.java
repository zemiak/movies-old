package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.domain.Movie;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("movieControllerAll")
@SessionScoped
public class MovieControllerAll extends AbstractMovieController {
    @PostConstruct
    public void init() {
        urls.setMovieController(this);
    }
    
    @Override
    public List<Movie> getItems() {
        if (null == movies) {
            movies = getFacade().findAll();
        }

        return movies;
    }
}
