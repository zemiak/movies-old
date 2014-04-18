package com.zemiak.movies.admin.beans;

import com.zemiak.movies.domain.Language;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Stateless
public class LanguageFacade extends AbstractFacade<Language> {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LanguageFacade() {
        super(Language.class);
    }
    
}
