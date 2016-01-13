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
public class RecentlyAddedGenre extends Genre {
    private static final long serialVersionUID = 1L;
    private static final int RECENTLY_ADDED_MONTHS = 6;

    @Inject MovieService service;
    @PersistenceContext EntityManager em;

    @PostConstruct
    public void init() {
        setId(-1);
        setName("Recently Added");

        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, RECENTLY_ADDED_MONTHS);

        List<Movie> movies = new ArrayList<>();
        service.all().stream().filter((movie) -> (null != movie.getCreated() && movie.getCreated().after(cal.getTime()))).forEach((movie) -> {
            em.detach(movie);
            movie.setGenre(this);
            movies.add(movie);
        });
        Collections.sort(movies, (Movie o1, Movie o2) -> o1.getCreated().compareTo(o2.getCreated()) * -1);

        this.setMovieList(movies);
    }
}
