package com.zemiak.movies.service.ui.play;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import java.io.Serializable;
import java.util.*;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("searchViewForm")
public class SearchViewForm implements Serializable {
    private final List<Serie> series = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();

    private String query;
    private Integer months;
    private Integer years;

    @Inject private MovieService movieService;
    @Inject private SerieService serieService;

    public void search() {
        if (null != query) {
            searchByQuery(query);
        }

        if (null != months) {
            searchByMonths(months);
        }

        if (null != years) {
            searchByYears(years);
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

    public String getTitle() {
        return null == query ? (months + " months old") : query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    private void searchByYears(Integer years) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());

        List<Movie> movieResults = new ArrayList<>();
        movieService.all().stream().filter((movie) -> (null != movie.getYear() && movie.getYear() >= (cal.get(Calendar.YEAR) - years))).forEach((movie) -> {
            movieResults.add(movie);
        });
        Collections.sort(movieResults, (Movie o1, Movie o2) -> o1.getCreated().compareTo(o2.getCreated()) * -1);
        movies.addAll(movieResults);
    }
}
