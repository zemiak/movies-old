package com.zemiak.movies.service;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.domain.Language;
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
public class LanguageService {
    @PersistenceContext
    private EntityManager em;
    
    public List<Language> all() {
        Query query = em.createQuery("SELECT l FROM Language l ORDER by l.displayOrder");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        return query.getResultList();
    }
    
    public void save(Language entity) {
        Language target = null;
        
        if (null != entity.getId()) {
            target = em.find(Language.class, entity.getId());
        }
        
        if (null == target) {
            em.persist(entity);
        } else {
            target.copyFrom(entity);
        }
    }
    
    public Language find(String id) {
        return em.find(Language.class, id);
    }

    public void remove(String entityId) {
        em.remove(em.find(Language.class, entityId));
    }
    
    public void clearCache(@Observes CacheClearEvent event) {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    
    public List<Language> getByExpression(final String text) {
        List<Language> res = new ArrayList<>();
        
        for (Language entry: all()) {
            if (entry.getName().toLowerCase().contains(text.toLowerCase())) {
                res.add(entry);
            }
        }
        
        return res;
    }
}
