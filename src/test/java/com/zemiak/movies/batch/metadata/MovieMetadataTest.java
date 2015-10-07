package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.movies.MovieMetadata;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MovieMetadataTest {

    public MovieMetadataTest() {

    }

    private Movie got, mash, spiderman;
    private MovieMetadata gotMeta, mashMeta, spidermanMeta;

    @Before
    public void initData() {
        Genre genre = new Genre();
        genre.setId(-1);
        genre.setName("S/F");

        spiderman = new Movie(); // 65 Spiderman parts
        spiderman.setName("Vyzva rude lebky");
        spiderman.setDisplayOrder(62);
        spiderman.setGenre(genre);

        got = new Movie(); // 40 GoT parts
        got.setName("48 The Watchers on the Wall");
        got.setDisplayOrder(409);
        got.setGenre(genre);

        mash = new Movie(); // 110 MASH parts
        mash.setName("049 General za usvitu");
        mash.setDisplayOrder(49);
        mash.setGenre(genre);

        spidermanMeta = new MovieMetadata(spiderman);
        spidermanMeta.setName("Vyzva rude lebky");
        spidermanMeta.setNiceDisplayOrder("62");
        spidermanMeta.setGenre("S/F");

        gotMeta = new MovieMetadata(got);
        gotMeta.setName("39 48 The Watchers on the Wall");
        gotMeta.setNiceDisplayOrder("48");
        gotMeta.setGenre("S/F");

        mashMeta = new MovieMetadata(mash);
        mashMeta.setName("049 049 General za usvitu");
        mashMeta.setNiceDisplayOrder("049");
        mashMeta.setGenre("S/F");
    }

    @Test
    public void spidermanNameNotOk() {
        assertFalse(spidermanMeta.isNameEqual());
    }

    @Test
    public void spidermanMp4MovieName() {
        assertEquals("62 Vyzva rude lebky", spidermanMeta.getMovieName());
    }

    @Test
    public void spidermanMetadataEqual() {
        assertFalse(spidermanMeta.isMetadataEqual());
    }

    @Test
    public void mashNameOk() {
        assertFalse(spidermanMeta.isNameEqual());
    }

    @Test
    public void mashMp4MovieName() {
        assertEquals("049 049 General za usvitu", mashMeta.getMovieName());
    }

    @Test
    public void mashMetadataEqual() {
        assertTrue(mashMeta.isMetadataEqual());
    }
}
