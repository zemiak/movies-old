package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.service.description.SeriesDescriptionReader;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GameOfThronesTest {
    private static final String GOT101 = "Winter Is Coming: King Robert Baratheon majestically arrives in Winterfell, the home of his old and trusted friend, Eddard Stark, Warden of the North, with an important offer. On the eastern continent, the dispossessed Princess Daenerys Targaryen marries Khal Drogo, a warlord of the Dothraki with tens of thousands of warriors at his command. Her brother, Viserys, callously plans to win Drogo's allegiance with the marriage, so that he may return home to Westeros and reclaim the Iron Throne, which was seized by force from his father by Robert. In the frozen lands, beyond the Wall, the wildlings are on the move to the alarm of the Night's Watch. But something else is stirring even further north.";
    private static final String GOT410 = "The Children: ...";
    private static final Integer ID = 1000;

    public GameOfThronesTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testAccepts() {
        final SeriesDescriptionReader instance = new SeriesDescriptionReader();
        final Movie m1 = new Movie();

        assertFalse(instance.accepts(m1));

        final Serie s1 = new Serie();
        s1.setId(Integer.SIZE - 1);
        m1.setSerie(s1);

        assertFalse(instance.accepts(m1));

        final Serie s2 = new Serie();
        s2.setId(ID);
        m1.setSerie(s2);
        m1.setDisplayOrder(101);

        assertTrue(instance.accepts(m1));
    }

    @Test
    public void testGetDescription() {
        final SeriesDescriptionReader instance = new SeriesDescriptionReader();

        final Serie s2 = new Serie();
        s2.setId(ID);

        final Movie m1 = new Movie();
        m1.setSerie(s2);
        m1.setDisplayOrder(0);

        assertNull(instance.getDescription(m1));

        m1.setDisplayOrder(101);
        assertNotNull(instance.getDescription(m1));
        assertEquals(GOT101, instance.getDescription(m1));

        m1.setDisplayOrder(410);
        assertNotNull(instance.getDescription(m1));
        assertEquals(GOT410, instance.getDescription(m1));

        m1.setDisplayOrder(2010);
        assertNull(instance.getDescription(m1));
    }
}
