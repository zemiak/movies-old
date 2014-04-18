package com.zemiak.movies.admin.beans;

import com.zemiak.movies.domain.Serie;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Stateless
public class SerieFacade extends AbstractFacade<Serie> {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SerieFacade() {
        super(Serie.class);
    }
    
}
