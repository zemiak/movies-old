package com.zemiak.movies.service;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.strings.Encodings;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

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
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public List<Movie> getNewMovies() {
        Query query = em.createQuery("SELECT l FROM Movie l WHERE (l.genreId = :genreNew1 OR l.genreId IS NULL) ORDER BY l.genreId, l.serieId, l.displayOrder");
        query.setParameter("genreNew1", em.find(Genre.class, 0));
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public List<Movie> getSerieMovies(Serie serie) {
        Query query = em.createQuery("SELECT l FROM Movie l WHERE l.serieId IS NULL OR l.serieId = :serie ORDER BY l.genreId, l.serieId, l.displayOrder");
        query.setParameter("serie", serie);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public List<Movie> getGenreMovies(Genre genre) {
        Query query = em.createQuery("SELECT l FROM Movie l WHERE l.genreId IS NULL OR l.genreId = :genre ORDER by l.genreId, l.serieId, l.displayOrder");
        query.setParameter("genre", genre);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
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
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
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
}