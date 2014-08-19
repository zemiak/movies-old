package com.zemiak.movies.service.jsp;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import com.zemiak.movies.service.backbonerest.MovieDTO;
import com.zemiak.movies.service.backbonerest.SerieDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class SearchService implements Serializable {
    private final List<SerieDTO> series = new ArrayList<>();
    private final List<MovieDTO> movies = new ArrayList<>();
    
    private final MovieService movieService;
    private final SerieService serieService;

    public SearchService() {
        CDILookup lookup = new CDILookup();
        movieService = lookup.lookup(MovieService.class);
        serieService = lookup.lookup(SerieService.class);
    }
    
    public void search(HttpServletRequest request) {
        if (null != request.getParameter("query")) {
            searchByQuery(request.getParameter("query"));
        }
        
        if (null != request.getParameter("months")) {
            searchByMonths(Integer.valueOf(request.getParameter("months")));
        }
    }

    private void searchByQuery(String query) {
        series.clear();
        serieService.getByExpression(query).stream().forEach((serie) -> {
            series.add(new SerieDTO(serie));
        });
        
        movies.clear();
        movieService.getByExpression(query).stream().forEach((movie) -> {
            movies.add(new MovieDTO(movie));
        });
    }

    private void searchByMonths(Integer months) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -months);
        
        List<SerieDTO> serieResults = new ArrayList<>();
        serieService.all().stream().filter((serie) -> (null != serie.getCreated() && serie.getCreated().after(cal.getTime()))).forEach((serie) -> {
            serieResults.add(new SerieDTO(serie));
        });
        Collections.sort(serieResults, (SerieDTO o1, SerieDTO o2) -> o1.getCreated().compareTo(o2.getCreated()) * -1);
        series.addAll(serieResults);
        
        List<MovieDTO> movieResults = new ArrayList<>();
        movieService.all().stream().filter((movie) -> (null != movie.getCreated() && movie.getCreated().after(cal.getTime()))).forEach((movie) -> {
            movieResults.add(new MovieDTO(movie));
        });
        Collections.sort(movieResults, (MovieDTO o1, MovieDTO o2) -> o1.getCreated().compareTo(o2.getCreated()) * -1);
        movies.addAll(movieResults);
    }

    public List<SerieDTO> getSeries() {
        return series;
    }

    public List<MovieDTO> getMovies() {
        return movies;
    }
}
