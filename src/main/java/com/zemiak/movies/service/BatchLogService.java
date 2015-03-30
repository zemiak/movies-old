package com.zemiak.movies.service;

import com.zemiak.movies.domain.BatchLog;
import com.zemiak.movies.domain.CacheClearEvent;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class BatchLogService {
    @PersistenceContext
    private EntityManager em;

    public List<BatchLog> all() {
        TypedQuery<BatchLog> query = em.createNamedQuery("BatchLog.findAll", BatchLog.class);

        return query.getResultList();
    }

    public void clearCache(@Observes CacheClearEvent event) {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    public BatchLog create(final String text) {
        BatchLog res = create();
        res.setText(text);

        return res;
    }

    public BatchLog find(final Integer id) {
        return em.find(BatchLog.class, id);
    }

    public BatchLog create() {
        BatchLog res = new BatchLog();
        res.setText("");
        em.persist(res);

        return res;
    }
}
