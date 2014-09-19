package com.zemiak.movies.service.jsp;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.lookup.CDILookup;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class MovieService implements Serializable {
    private final com.zemiak.movies.service.MovieService service;
    private Integer genreId;
    private Integer serieId;
    private Integer id;
    private Movie movie;

    public MovieService() {
        CDILookup lookup = new CDILookup();
        service = lookup.lookup(com.zemiak.movies.service.MovieService.class);
    }

    public List<Movie> getByGenreId() {
        return service.all().stream()
                .filter(entry -> genreId.equals(entry.getGenreId().getId()) && entry.isEmptySerie())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Movie> getBySerieId() {
        return service.all().stream()
                .filter(movie -> null != movie.getSerieId() && serieId.equals(movie.getSerieId().getId()))
                .sorted()
                .collect(Collectors.toList());
    }

    public void setSerieId(HttpServletRequest request) {
        if (null != request.getParameter("id")) {
            serieId = Integer.valueOf(request.getParameter("id"));
        }
    }

    public void setGenreId(HttpServletRequest request) {
        if (null != request.getParameter("id")) {
            genreId = Integer.valueOf(request.getParameter("id"));
        }
    }

    public void setMovieId(HttpServletRequest request) {
        if (null != request.getParameter("id")) {
            id = Integer.valueOf(request.getParameter("id"));
            movie = service.find(id);
        }
    }

    public Movie getMovie() {
        return movie;
    }
}
