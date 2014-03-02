package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.domain.Movie;

import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named("movieControllerNew")
@SessionScoped
public class MovieControllerNew extends MovieController implements Serializable {
    @Override
    public List<Movie> getItems() {
        if (null == movies) {
            movies = getFacade().findAllNew();
        }

        return movies;
    }
}