package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MovieMetadataTest {

    public MovieMetadataTest() {

    }

    private Movie got, mash, spiderman;
    private MovieMetadata gotMeta, mashMeta, spidermanMeta;
    private Serie mashSerie;

    @Before
    public void initData() {
        Genre genre = new Genre();
        genre.setId(-1);
        genre.setName("S/F");

        spiderman = new Movie(); // 65 Spiderman parts
        spiderman.setName("Vyzva rude lebky");
        spiderman.setDisplayOrder(62);
        spiderman.setGenre(genre);
        spiderman.setYear(1920);

        mashSerie = new Serie();
        mashSerie.setName("MASH");
        mashSerie.setId(10);

        got = new Movie(); // 40 GoT parts
        got.setName("48 The Watchers on the Wall");
        got.setDisplayOrder(409);
        got.setGenre(genre);

        mash = new Movie(); // 110 MASH parts
        mash.setName("049 General za usvitu");
        mash.setDisplayOrder(49);
        mash.setGenre(genre);
        mash.setYear(1991);
        mash.setSerie(mashSerie);

        spidermanMeta = new MovieMetadata(spiderman);
        spidermanMeta.setName("Vyzva rude lebky");
        spidermanMeta.setNiceDisplayOrder("62");
        spidermanMeta.setGenre("S/F");

        gotMeta = new MovieMetadata(got);
        gotMeta.setName("39 48 The Watchers on the Wall");
        gotMeta.setNiceDisplayOrder("48");
        gotMeta.setGenre("S/F");

        mashMeta = new MovieMetadata(mash);
        mashMeta.setName("049 General za usvitu");
        mashMeta.setNiceDisplayOrder("49");
        mashMeta.setGenre("S/F");
        mashMeta.setYear(1991);
    }

    @Test
    public void spidermanNameNotOk() {
        assertFalse(spidermanMeta.isNameEqual());
    }

    @Test
    public void spidermanMp4MovieName() {
        assertEquals("Vyzva rude lebky (1920)", spidermanMeta.getMovieName());
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
        assertEquals("049 General za usvitu", mashMeta.getMovieName());
    }

    @Test
    public void mashMetadataEqual() {
        boolean value = mashMeta.isMetadataEqual();
        assertTrue(value);
    }
}
