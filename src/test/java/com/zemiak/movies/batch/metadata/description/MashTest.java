
package com.zemiak.movies.batch.metadata.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vasko
 */
public class MashTest {

    public MashTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testAccepts() {
        final Mash instance = new Mash();
        final Movie m1 = new Movie();

        assertFalse(instance.accepts(m1));

        final Serie s1 = new Serie();
        s1.setId(Integer.SIZE - 1);
        m1.setSerieId(s1);

        assertFalse(instance.accepts(m1));

        final Serie s2 = new Serie();
        s2.setId(Serie.MASH_ID);
        m1.setSerieId(s2);

        assertTrue(instance.accepts(m1));
    }

    @Test
    public void testGetDescription() {
        final Mash instance = new Mash();
        final Movie m1 = new Movie();
        m1.setDisplayOrder(0);

        assertNull(instance.getDescription(m1));

        m1.setDisplayOrder(1);
        assertNotNull(instance.getDescription(m1));

        m1.setDisplayOrder(256);
        assertNotNull(instance.getDescription(m1));

        m1.setDisplayOrder(257);
        assertNull(instance.getDescription(m1));
    }
}
