package com.zemiak.movies.thumbnail;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.thumbnail.ImdbThumbnail;
import java.io.File;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ImdbThumbnailIT {
    private Movie movie;
    private ImdbThumbnail thumbnail;

    @Before
    public void setUp() {
        movie = new Movie();
        thumbnail = new ImdbThumbnail();
    }

    @Test
    public void testAccepts() {
        movie.setUrl("http://www.imdb.com/title/tt0133093/?ref_=fn_al_tt_1");
        assertTrue(thumbnail.accepts(movie));

        movie.setUrl("http://www.imdb.com/title/tt0096895/?ref_=fn_al_tt_1");
        assertTrue(thumbnail.accepts(movie));

        movie.setUrl("http://www.csfd.cz/film/9499-matrix/");
        assertFalse(thumbnail.accepts(movie));

        movie.setUrl("http://www.csfd.cz/film/1069-batman/");
        assertFalse(thumbnail.accepts(movie));
    }

    @Test
    public void testDownload() {
        String fileName = "/tmp/" + Math.random() + ".jpg";
        movie.setUrl("http://www.imdb.com/title/tt0096895/?ref_=fn_al_tt_1");
        thumbnail.setImageFileName(fileName);
        thumbnail.process(movie);

        File file = new File(fileName);
        assertTrue(file.isFile());
        assertTrue(file.canRead());
    }
}