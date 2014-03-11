package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import java.util.Map;
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
public class ImdbTest {
    private Movie movie;
    private Imdb reader;
    
    public ImdbTest() {
    }
    
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
    public void testGetDescription() {
        movie.setUrl("http://www.imdb.com/title/tt0133093/?ref_=fn_al_tt_1");
        String description = reader.getDescription(movie);
        
        assertNotNull(description);
        assertFalse(description.trim().isEmpty());
    }

    @Test
    public void testGetUrlCandidates() {
        Map<String, String> urls = reader.getUrlCandidates("Batman");
        
        assertTrue(urls.size() > 3);
    }
    
}
