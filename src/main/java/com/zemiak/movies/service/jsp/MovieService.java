package com.zemiak.movies.service.jsp;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.backbonerest.MovieDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class MovieService implements Serializable {
    private final com.zemiak.movies.service.MovieService service;
    private Integer genreId;
    private Integer serieId;

    public MovieService() {
        CDILookup lookup = new CDILookup();
        service = lookup.lookup(com.zemiak.movies.service.MovieService.class);
    }

    public List<MovieDTO> getByGenreId() {
        List<MovieDTO> results = new ArrayList<>();
        service.all().stream().filter(movie -> genreId.equals(movie.getGenreId().getId()) && null == movie.getSerieId()).forEach((serie) -> {
            results.add(new MovieDTO(serie));
        });
        
        return results;
    }
    
    public List<MovieDTO> getBySerieId() {
        List<MovieDTO> results = new ArrayList<>();
        service.all().stream().filter(movie -> null != movie.getSerieId() && serieId.equals(movie.getSerieId().getId())).forEach((serie) -> {
            results.add(new MovieDTO(serie));
        });
        
        return results;
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
}
