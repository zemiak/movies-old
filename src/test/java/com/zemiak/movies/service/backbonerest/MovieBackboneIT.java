package com.zemiak.movies.service.backbonerest;

import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.backbonerest.specialmovie.Last6MonthsGenre;
import com.zemiak.movies.strings.Encodings;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author vasko
 */
@RunWith(Arquillian.class)
public class MovieBackboneIT {
    @Inject
    MovieBackbone backboneMovies;
    
    @Inject
    MovieService movies;
    
    public MovieBackboneIT() {
    }
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(MovieBackboneIT.class, Last6MonthsGenre.class, Encodings.class)
                .addPackage("com.zemiak.movies.domain")
                .addPackage("com.zemiak.movies.service")
                .addPackage("com.zemiak.movies.service.backbonerest")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/create-script-source.sql", "create-script-source.sql")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAll() throws Exception {
    }

    @Test
    public void testSearch() throws Exception {
    }

    @Test
    public void testFind() throws Exception {
    }

    @Test
    public void testCreate() throws Exception {
    }

    @Test
    public void testUpdate() throws Exception {
    }

    @Test
    public void testDelete() throws Exception {
    }

    @Test
    public void testVersion() throws Exception {
    }

    @Test
    @UsingDataSet("6months.yml")
    public void testGetLastNMonthsMovies() throws Exception {
        assertEquals(5, movies.all().size());
        
        List<MovieDTO> dtos = backboneMovies.getLastNMonthsMovies(6);
        
        for (MovieDTO movie: dtos) {
            System.err.println(String.format("%s - %s", movie.getName(), movie.getCreated()));
        }
        
        assertEquals(2, dtos.size());
        assertTrue(dtos.get(0).getName().equals("May2") || dtos.get(1).getName().equals("May2"));
        assertTrue(dtos.get(0).getName().equals("July16") || dtos.get(1).getName().equals("July16"));
    }
    
}
