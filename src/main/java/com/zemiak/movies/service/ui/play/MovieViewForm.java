package com.zemiak.movies.service.ui.play;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.ui.admin.JsfMessages;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("movieViewForm")
public class MovieViewForm implements Serializable {
    @Inject private MovieService service;
    @Inject private GenreViewForm genreWebService;
    @Inject private SerieViewForm serieWebService;

    private Integer id;
    private Movie movie;

    public List<Movie> getByGenreId() {
        return service.all().stream()
                .filter(entry -> genreWebService.getId().equals(entry.getGenreId().getId()) && entry.isEmptySerie())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Movie> getBySerieId() {
        return service.all().stream()
                .filter(movie -> null != movie.getSerieId() && serieWebService.getId().equals(movie.getSerieId().getId()))
                .sorted()
                .collect(Collectors.toList());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String check() {
        if (null != id) {
            movie = service.find(id);
        }

        if (null == id || null == movie) {
            JsfMessages.addErrorMessage("Movie #" + id + " cannot be found");
            return "index";
        }

        return null;
    }

    public Movie getMovie() {
        return movie;
    }
}
