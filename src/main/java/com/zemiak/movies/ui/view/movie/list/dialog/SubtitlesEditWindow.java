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
public class SubtitlesEditWindow extends AbstractLanguageEditWindow {
    public SubtitlesEditWindow() {
        super();
    }
    
    @Override
    protected String caption() {
        return "Subtitles Language";
    }
    
    @Override
    public void process(List<Movie> movies) {
        if (language.getValue() == null) {
            Notification.show("Please, select a language, first", Notification.Type.WARNING_MESSAGE);
            return;
        }
        
        for (Movie movie: movies) {
            movie.setSubtitles((Language) language.getValue());
            getMovieService().save(movie);
        }
    }

    @Inject private MovieService movieService;
    @Override
    protected MovieService getMovieService() {
        return movieService;
    }

    @Inject private LanguageService languageService;
    @Override
    protected LanguageService getLanguageService() {
        return languageService;
    }

    @PersistenceContext private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
