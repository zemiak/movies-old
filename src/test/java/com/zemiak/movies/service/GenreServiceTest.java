package com.zemiak.movies.service;

import com.zemiak.movies.domain.Genre;
import java.sql.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.ggtools.junit.categories.Slow;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
//@Category(value = Slow.class)
public class GenreServiceTest {
    
    public GenreServiceTest() {
    }
    
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(GenreService.class, GenreServiceTest.class)
                .addPackage("com.zemiak.movies.domain")
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
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    GenreService service;

    @Test
    @UsingDataSet("all.yml")
    public void all() throws Exception {
        List<Genre> genres = service.all();
        assertEquals(3, genres.size());
    }
    
    @Test
    @ShouldMatchDataSet(value = "saved-genre.yml", excludeColumns = "id")
    public void save() {
        Genre genre = new Genre();
        genre.setCreated(Date.valueOf("2014-01-01"));
        genre.setDisplayOrder(3);
        genre.setName("Horror");
        genre.setPictureFileName("genres/horror.png");
        service.save(genre);
    }
}
