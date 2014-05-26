package com.zemiak.movies.service;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.strings.Encodings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author vasko
 */
@Stateless
public class MovieService {
    @PersistenceContext
    private EntityManager em;
    
    public List<Movie> all() {
        Query query = em.createQuery("SELECT l FROM Movie l ORDER BY l.genreId, l.serieId, l.displayOrder");
        
        return query.getResultList();
    }
    
    public List<Movie> getNewMovies() {
        Query query = em.createQuery("SELECT l FROM Movie l WHERE (l.genreId = :genreNew1 OR l.genreId IS NULL) ORDER BY l.genreId, l.serieId, l.displayOrder");
        query.setParameter("genreNew1", em.find(Genre.class, 0));
        
        return query.getResultList();
    }
    
    public List<Movie> getSerieMovies(Serie serie) {
        Query query = em.createQuery("SELECT l FROM Movie l WHERE l.serieId IS NULL OR l.serieId = :serie ORDER BY l.genreId, l.serieId, l.displayOrder");
        query.setParameter("serie", serie);
        
        return query.getResultList();
    }
    
    public List<Movie> getGenreMovies(Genre genre) {
        Query query = em.createQuery("SELECT l FROM Movie l WHERE l.genreId IS NULL OR l.genreId = :genre ORDER by l.genreId, l.serieId, l.displayOrder");
        query.setParameter("genre", genre);
        
        return query.getResultList();
    }
    
    public void save(Movie entity) {
        Movie target = null;
        
        if (null != entity.getId()) {
            target = em.find(Movie.class, entity.getId());
        }
        
        if (null == target) {
            em.persist(entity);
        } else {
            target.copyFrom(entity);
        }
    }
    
    public Movie find(Integer id) {
        return em.find(Movie.class, id);
    }

    public void remove(Integer entityId) {
        em.remove(em.find(Movie.class, entityId));
    }
    
    public void clearCache(@Observes CacheClearEvent event) {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    public List<Movie> getByExpression(final String text) {
        List<Movie> res = new ArrayList<>();
        String textAscii = Encodings.toAscii(text.trim().toLowerCase());
        
        for (Movie entry: all()) {
            String name = (null == entry.getName() ? "" 
                    : Encodings.toAscii(entry.getName().trim().toLowerCase()));
            
            if (name.contains(textAscii)) {
                res.add(entry);
            }
        }
        
        return res;
    }

    public List<Movie> getLastMovies(int count) {
        Query query = em.createQuery("SELECT l FROM Movie l ORDER BY l.id DESC");
        query.setMaxResults(count);
        
        return query.getResultList();
    }
    
    public List<Movie> findAllNew() {
        List<Movie> res = new ArrayList<>();
        
        for (Movie movie: all()) {
            if (null == movie.getGenreId() || movie.getGenreId().isEmpty()) {
                res.add(movie);
            }
        }
        
        return res;
    }

    public Movie findByFilename(String fileName) {
        Query query = em.createNamedQuery("Movie.findByFileName");
        query.setParameter("fileName", fileName);
        Movie movie;
        
        try {
            movie = (Movie) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            movie = null;
        }
        
        return movie;
    }

    public Movie create(String newFile) {
        final Movie movie = new Movie();
        final String baseFileName = new File(newFile).getName();
        final String name = baseFileName.substring(0, baseFileName.lastIndexOf("."));

        movie.setFileName(newFile);
        movie.setGenreId(em.getReference(Genre.class, 0));
        movie.setSerieId(em.getReference(Serie.class, 0));
        movie.setName(name);
        movie.setPictureFileName(name + ".jpg");
        //em.persist(name);
        
        return movie;
    }

    public void mergeAndSave(Movie movie) {
        em.merge(movie);
    }
}