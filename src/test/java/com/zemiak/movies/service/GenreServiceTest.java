package com.zemiak.movies.service;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author vasko
 */
@RunWith(Arquillian.class)
public class GenreServiceTest {
    
    public GenreServiceTest() {
    }
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(Genre.class, GenreService.class, Movie.class, Language.class,
                        GenreServiceTest.class, CacheClearEvent.class, Serie.class)
                .addAsManifestResource("META-INF/persistence.xml",
                        "persistence.xml")
                .addAsManifestResource("META-INF/orm.xml", "orm.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @PersistenceContext
    private EntityManager em;

    @Test
    @UsingDataSet("data/integration-tests.yml")
    public void testAll() throws Exception {
        List<Genre> genres = em.createNamedQuery("Genre.findAll").getResultList();
        assertEquals(3, genres.size());
    }

    @Test
    public void testSave() throws Exception {
    }

    @Test
    public void testFind() throws Exception {
    }

    @Test
    public void testRemove() throws Exception {
    }

    @Test
    public void testClearCache() throws Exception {
    }

    @Test
    public void testGetByExpression() throws Exception {
    }

    @Test
    public void testGetMoviesWithoutSerie() throws Exception {
    }
}
