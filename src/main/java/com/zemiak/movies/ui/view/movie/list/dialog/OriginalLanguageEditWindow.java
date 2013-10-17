package com.zemiak.movies.ui.view.movie.list.dialog;

import com.vaadin.ui.Notification;
import com.zemiak.movies.boundary.LanguageService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Dependent
public class OriginalLanguageEditWindow extends AbstractLanguageEditWindow {
    @PersistenceContext private EntityManager em;
    @Inject private MovieService movieService;
    @Inject private LanguageService languageService;
    
    public OriginalLanguageEditWindow() {
        super();
    }

    @Override
    protected String caption() {
        return "Original Language";
    }
    
    @Override
    public void process(List<Movie> movies) {
        if (language.getValue() == null) {
            Notification.show("Please, select a language, first", Notification.Type.WARNING_MESSAGE);
            return;
        }
        
        for (Movie movie: movies) {
            movie.setLanguage((Language) language.getValue());
            getMovieService().save(movie);
        }
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
