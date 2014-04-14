package com.zemiak.movies.service;

import com.zemiak.movies.domain.CacheClearEvent;
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
public class SerieService {
    @PersistenceContext
    private EntityManager em;
    
    public List<Serie> all() {
        Query query = em.createQuery("SELECT l FROM Serie l ORDER by l.displayOrder");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public void save(final Serie entity) {
        Serie target = null;
        
        if (null != entity.getId()) {
            target = em.find(Serie.class, entity.getId());
        }
        
        if (null == target) {
            em.persist(entity);
        } else {
            target.copyFrom(entity);
        }
    }
    
    public Serie find(final Integer id) {
        return em.find(Serie.class, id);
    }

    public void remove(final Integer entityId) {
        em.remove(em.find(Serie.class, entityId));
    }
    
    public void clearCache(@Observes final CacheClearEvent event) {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    public List<Serie> getByExpression(final String text) {
        List<Serie> res = new ArrayList<>();
        String textAscii = Encodings.toAscii(text.trim().toLowerCase());
        
        for (Serie entry: all()) {
            String name = (null == entry.getName() ? "" 
                    : Encodings.toAscii(entry.getName().trim().toLowerCase()));
            
            if (name.contains(textAscii)) {
                res.add(entry);
            }
        }
        
        return res;
    }
}