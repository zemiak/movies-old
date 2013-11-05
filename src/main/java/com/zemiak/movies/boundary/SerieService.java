package com.zemiak.movies.boundary;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
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
public class SerieService {
    @PersistenceContext
    private EntityManager em;
    
    public List<Serie> all() {
        Query query = em.createQuery("SELECT l FROM Serie l ORDER by l.displayOrder");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public void save(Serie entity) {
        Serie target = em.find(Serie.class, entity.getId());
        
        if (null == target) {
            em.persist(entity);
        } else {
            target.copyFrom(entity);
        }
    }
    
    public Serie find(Integer id) {
        return em.find(Serie.class, id);
    }

    public void remove(Integer entityId) {
        em.remove(em.find(Serie.class, entityId));
    }
    
    public void clearCache(@Observes CacheClearEvent event) {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    public List<Movie> getByExpression(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}