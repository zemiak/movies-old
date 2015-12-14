package com.zemiak.movies.year;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.scraper.Imdb;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ImdbYearIT {
    private Movie movie;
    private Imdb reader;

    @Before
    public void setUp() {
        movie = new Movie();
        reader = new Imdb();
    }

    @Test
    public void testSpectre() {
        movie.setUrl("http://www.imdb.com/title/tt2379713/?ref_=fn_al_tt_1");
        assertEquals(Integer.valueOf(2015), reader.parseYear(movie));
    }

    @Test
    public void testHowToTrainYourDragon() {
        movie.setUrl("http://www.imdb.com/title/tt0892769/?ref_=fn_al_tt_1");
        assertEquals(Integer.valueOf(2010), reader.parseYear(movie));
    }
}
