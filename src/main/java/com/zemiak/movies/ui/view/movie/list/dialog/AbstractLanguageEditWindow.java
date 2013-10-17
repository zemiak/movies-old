package com.zemiak.movies.ui.view.movie.list.dialog;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.zemiak.movies.boundary.LanguageService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import java.util.List;

/**
 *
 * @author vasko
 */
abstract public class AbstractLanguageEditWindow extends AbstractEditWindow {
    protected ComboBox language;
    
    public AbstractLanguageEditWindow() {
        super();
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
    
    protected String caption() {
        return "Language";
    }

    @Override
    protected Component content() {
        FormLayout layout = new FormLayout();
        
        language = new ComboBox(caption());
        language.setWidth("10em");
        
        for (Language entry: getLanguageService().all()) {
            language.addItem(entry.getId());
            language.setItemCaption(entry.getId(), entry.getName());
        }
        
        layout.addComponent(language);
        
        return layout;
    }
    
    abstract protected MovieService getMovieService();
    abstract protected LanguageService getLanguageService();
}
