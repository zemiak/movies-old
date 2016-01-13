package com.zemiak.movies.service.ui.play;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.ui.admin.JsfMessages;
import java.io.Serializable;
import java.util.*;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("moviesViewForm")
@SessionScoped
public class MoviesViewForm implements Serializable {
    @Inject private MovieService service;
    private Integer id;
    private List<Movie> movies = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String check() {
        if (null == id || (1 != id && 2 != id)) {
            JsfMessages.addErrorMessage("Type #" + id + " is unknown");
            return "index";
        }

        return null;
    }

    public String getTitle() {
        return 1 == id ? "Recently Added" : "New Releases";
    }

    public List<Movie> getByType() {
        return 1 == id ? getRecentlyAdded() : getNewReleases();
    }

    private List<Movie> getRecentlyAdded() {
        return service.getLastMovies(64);
    }

    private List<Movie> getNewReleases() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());

        List<Movie> movies = new ArrayList<>();
        service.all().stream()
                .filter((movie) -> (null != movie.getYear() && movie.getYear() >= (cal.get(Calendar.YEAR) - 3)))
                .forEach((movie) -> {
                    movies.add(movie);
                });
        Collections.sort(movies, (Movie o1, Movie o2) -> o1.getYear().compareTo(o2.getYear()) * -1);

        return movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void filter() {
        movies = getByType();
    }
}
