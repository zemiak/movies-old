package com.zemiak.movies.description;

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
    private static final String MASH1 = "M*A*S*H - ÃvodnÃ­ epizoda: KorejskÃ½ chlapec Ho John je pÅijat ke studiu na Å¡kole v Maine, kde vystudoval Hawkeye. Aby sehnali penÃ­ze na jeho cestu, Hawkeye s Traperem uspoÅÃ¡dajÃ­ pÅes odpor Burnse a Å tabajzny loterii o vÃ­kend s nejhezÄÃ­ sestrou v Tokiu. VÃ­tÄzem se stane ke vÅ¡eobecnÃ©mu pÅekvapenÃ­ otec Mulcahy.";
    private static final String MASH256 = "Sbohem, na rozlouÄenou a amen: PoslednÃ­, dvouhodinovÃ½ speciÃ¡lnÃ­ dÃ­l zachycuje konec nemocnice MASH. Klinger se v nÄm oÅ¾enÃ­ s korejskou dÃ­vkou, Hawkeye pÅetrpÃ­ nejtÄÅ¾Å¡Ã­ psychickou krizi svÃ©ho vÃ¡leÄnÃ©ho Å¾ivota a nakonec se vÅ¡ichni vracejÃ­ domÅ¯. VÃ¡lka skonÄila.";
    private static final Integer ID = 1;

    public MashTest() {
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
        m1.setSerieId(s1);
        assertFalse(instance.accepts(m1));

        final Serie s2 = new Serie();
        s2.setId(ID);
        m1.setSerieId(s2);
        m1.setDisplayOrder(1);
        assertTrue(instance.accepts(m1));

        m1.setDisplayOrder(256);
        assertTrue(instance.accepts(m1));
    }

    @Test
    public void testGetDescription() {
        final SeriesDescriptionReader instance = new SeriesDescriptionReader();

        final Serie s2 = new Serie();
        s2.setId(ID);

        final Movie m1 = new Movie();
        m1.setSerieId(s2);
        m1.setDisplayOrder(0);

        assertNull(instance.getDescription(m1));

        m1.setDisplayOrder(1);
        assertNotNull(instance.getDescription(m1));
        assertEquals(MASH1, instance.getDescription(m1));

        m1.setDisplayOrder(256);
        assertNotNull(instance.getDescription(m1));
        assertEquals(MASH256, instance.getDescription(m1));

        m1.setDisplayOrder(257);
        assertNull(instance.getDescription(m1));
    }
}
