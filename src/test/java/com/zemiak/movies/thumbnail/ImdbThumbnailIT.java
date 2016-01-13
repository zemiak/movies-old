package com.zemiak.movies.thumbnail;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.scraper.Imdb;
import java.io.File;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ImdbThumbnailIT {
    private Movie movie;
    private Imdb reader;

    @Before
    public void setUp() {
        movie = new Movie();
        reader = new Imdb();
    }

    @Test
    public void testAccepts() {
        movie.setUrl("http://www.imdb.com/title/tt0133093/?ref_=fn_al_tt_1");
        assertTrue(reader.accepts(movie));

        movie.setUrl("http://www.imdb.com/title/tt0096895/?ref_=fn_al_tt_1");
        assertTrue(reader.accepts(movie));

        movie.setUrl("http://www.csfd.cz/film/9499-matrix/");
        assertFalse(reader.accepts(movie));

        movie.setUrl("http://www.csfd.cz/film/1069-batman/");
        assertFalse(reader.accepts(movie));
    }

    @Test
    public void testDownload() {
        String fileName = "/tmp/imdb-" + Math.random() + ".jpg";
        movie.setUrl("http://www.imdb.com/title/tt0470752/?ref_=hm_otw_t1");
        reader.setImageFileName(fileName);
        reader.processThumbnail(movie);

        File file = new File(fileName);
        assertNotNull("Filename is not null", file);
        assertTrue("Filename is a regular file", file.isFile());
        assertTrue("Filename can be read", file.canRead());
        assertTrue("Filename is not empty", file.length() > 0);
    }
}
