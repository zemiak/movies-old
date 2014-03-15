package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import java.util.List;
import net.ggtools.junit.categories.Integration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;

/**
 *
 * @author vasko
 */
@Category(value = Integration.class)
public class CsfdTest {
    private Movie movie;
    private Csfd reader;
    
    public CsfdTest() {
    }
    
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
        
        movie.setUrl("http://www.csfd.cz/film/9499-matrix/");
        assertTrue(reader.accepts(movie));
        
        movie.setUrl("http://www.csfd.cz/film/1069-batman/");
        assertTrue(reader.accepts(movie));
    }

    @Test
    public void testGetDescription() {
        movie.setUrl("http://www.csfd.cz/film/9499-matrix/");
        String description = reader.getDescription(movie);
        
        assertNotNull(description);
        assertFalse(description.trim().isEmpty());
    }

    @Test
    public void testGetUrlCandidates() {
        final List<UrlDTO> urls = reader.getUrlCandidates("Batman");
        
        assertTrue(urls.size() > 3);
    }
    
}
