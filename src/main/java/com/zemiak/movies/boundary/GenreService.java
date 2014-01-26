package com.zemiak.movies.boundary;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
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
public class GenreService {
    @PersistenceContext
    private EntityManager em;
    
    public List<Genre> all() {
        Query query = em.createQuery("SELECT l FROM Genre l ORDER by l.displayOrder");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public void save(Genre entity) {
        Genre target = em.find(Genre.class, entity.getId());
        
        if (null == target) {
            em.persist(entity);
        } else {
            target.copyFrom(entity);
        }
    }
    
    public Genre find(Integer id) {
        return em.find(Genre.class, id);
    }

    public void remove(String entityId) {
        em.remove(em.find(Genre.class, entityId));
    }
    
    public void clearCache(@Observes CacheClearEvent event) {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    public List<Genre> getByExpression(final String text) {
        List<Genre> res = new ArrayList<>();
        
        for (Genre entry: all()) {
            if (entry.getName().toLowerCase().contains(text.toLowerCase())) {
                res.add(entry);
            }
        }
        
        return res;
    }

    public List<Movie> getMoviesWithoutSerie(Genre genre) {
        Query query = em.createNamedQuery("Movie.findByGenreWithoutSerie");
        query.setParameter("genre", genre);
        
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return query.getResultList();
    }
}
