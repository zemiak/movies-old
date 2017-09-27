package com.zemiak.movies.thumbnail;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.scraper.Csfd;
import java.io.File;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CsfdThumbnailIT {
    private static final String MOVIE_URL = "https://www.csfd.cz/film/434887-wind-river/";

    private Movie movie;
    private Csfd reader;

    @Before
    public void setUp() {
        movie = new Movie();
        reader = new Csfd();
    }

    @Test
    public void testAccepts() {
        movie.setUrl("http://www.imdb.com/title/tt0133093/?ref_=fn_al_tt_1");
        assertFalse(reader.accepts(movie));

        movie.setUrl("http://www.imdb.com/title/tt0096895/?ref_=fn_al_tt_1");
        assertFalse(reader.accepts(movie));

        movie.setUrl(MOVIE_URL);
        assertTrue(reader.accepts(movie));

        movie.setUrl("http://www.csfd.cz/film/1069-batman/");
        assertTrue(reader.accepts(movie));
    }

    @Test
    public void testDownload() {
        String fileName = "/tmp/csfd-" + Math.random() + ".jpg";
        movie.setUrl(MOVIE_URL);
        reader.setImageFileName(fileName);
        reader.processThumbnail(movie);

        File file = new File(fileName);
        assertNotNull("Filename is not null", file);
        assertTrue("Filename is a regular file", file.isFile());
        assertTrue("Filename can be read", file.canRead());
        assertTrue("Filename is not empty", file.length() > 0);
    }
}
