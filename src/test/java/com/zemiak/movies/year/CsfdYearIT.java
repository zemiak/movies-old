package com.zemiak.movies.year;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.scraper.Csfd;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CsfdYearIT {
    private Movie movie;
    private Csfd reader;

    @Before
    public void setUp() {
        movie = new Movie();
        reader = new Csfd();
    }

    @Test
    public void testSpectre() {
        movie.setUrl("http://www.csfd.cz/film/328151-spectre/prehled/");
        assertEquals(Integer.valueOf(2015), reader.getYear(movie));
    }

    @Test
    public void testHowToTrainYourDragon() {
        movie.setUrl("http://www.csfd.cz/film/234768-jak-vycvicit-draka/prehled/");
        assertEquals(Integer.valueOf(2010), reader.getYear(movie));
    }
}
