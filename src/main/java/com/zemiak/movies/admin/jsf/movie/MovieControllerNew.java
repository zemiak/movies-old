package com.zemiak.movies.admin.jsf.movie;

import com.zemiak.movies.domain.Movie;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("movieControllerNew")
@RequestScoped
public class MovieControllerNew extends AbstractMovieController implements Serializable {
    @PostConstruct
    public void init() {
        urls.setMovieController(this);
    }

    @Override
    public List<Movie> getItems() {
        if (null == movies) {
            movies = getFacade().findAllNew();
        }

        return movies;
    }
}
