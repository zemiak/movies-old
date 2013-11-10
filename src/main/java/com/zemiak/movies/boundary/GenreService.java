package com.zemiak.movies.boundary;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.domain.Genre;
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

    public List<Genre> getByExpression(String text) {
        Query query = em.createQuery("SELECT l FROM Genre l WHERE (l.name LIKE :expr) ORDER BY l.name");
        query.setParameter("expr", text);
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
}
