package com.zemiak.movies.service;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
public class NewReleasesGenre extends Genre {
    private static final long serialVersionUID = 1L;
    private static final int NEW_RELEASES_YEARS = 2;

    @Inject MovieService service;
    @PersistenceContext EntityManager em;

    @PostConstruct
    public void init() {
        setId(-2);
        setName("New Releases");

        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());

        List<Movie> movies = new ArrayList<>();
        service.all().stream().filter((movie) -> (null != movie.getYear() && movie.getYear() >= (cal.get(Calendar.YEAR) - NEW_RELEASES_YEARS))).forEach((movie) -> {
            em.detach(movie);
            movie.setGenre(this);
            movies.add(movie);
        });
        Collections.sort(movies, (Movie o1, Movie o2) -> o1.getCreated().compareTo(o2.getCreated()) * -1);

        this.setMovieList(movies);
    }
}
