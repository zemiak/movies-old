package com.zemiak.movies.boundary;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.util.List;
import javax.ejb.Stateless;
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
        Query query = em.createQuery("SELECT l FROM Movie l ORDER by l.genreId, l.serieId, l.displayOrder");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public List<Movie> getNewMovies() {
        Query query = em.createQuery("SELECT l FROM Movie l ORDER by l.genreId, l.serieId, l.displayOrder");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public List<Movie> getSerieMovies(Serie serie) {
        Query query = em.createQuery("SELECT l FROM Movie l WHERE l.serieId IS NULL OR l.serieId = :serie ORDER by l.genreId, l.serieId, l.displayOrder");
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
        Movie target = em.find(Movie.class, entity.getId());
        
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
}