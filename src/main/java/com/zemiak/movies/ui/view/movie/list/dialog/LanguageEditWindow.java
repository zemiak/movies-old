package com.zemiak.movies.ui.view.movie.list.dialog;

import com.zemiak.movies.boundary.LanguageService;
import com.zemiak.movies.boundary.MovieService;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Dependent
public class LanguageEditWindow extends AbstractLanguageEditWindow {
    @PersistenceContext private EntityManager em;
    @Inject private MovieService movieService;
    @Inject private LanguageService languageService;
    
    public LanguageEditWindow() {
        super();
    }

    @Override
    protected MovieService getMovieService() {
        return movieService;
    }

    @Override
    protected LanguageService getLanguageService() {
        return languageService;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
