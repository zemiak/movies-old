package com.zemiak.movies.service.jsp;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import java.io.Serializable;
import java.util.*;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class SearchWebService implements Serializable {
    private final List<Serie> series = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();

    private final MovieService movieService;
    private final SerieService serieService;

    public SearchWebService() {
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
            series.add(serie);
        });

        movies.clear();
        movieService.getByExpression(query).stream().forEach((movie) -> {
            movies.add(movie);
        });
    }

    private void searchByMonths(Integer months) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -months);

        List<Serie> serieResults = new ArrayList<>();
        serieService.all().stream().filter((serie) -> (null != serie.getCreated() && serie.getCreated().after(cal.getTime()))).forEach((serie) -> {
            serieResults.add(serie);
        });
        Collections.sort(serieResults, (Serie o1, Serie o2) -> o1.getCreated().compareTo(o2.getCreated()) * -1);
        series.addAll(serieResults);

        List<Movie> movieResults = new ArrayList<>();
        movieService.all().stream().filter((movie) -> (null != movie.getCreated() && movie.getCreated().after(cal.getTime()))).forEach((movie) -> {
            movieResults.add(movie);
        });
        Collections.sort(movieResults, (Movie o1, Movie o2) -> o1.getCreated().compareTo(o2.getCreated()) * -1);
        movies.addAll(movieResults);
    }

    public List<Serie> getSeries() {
        return series;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
