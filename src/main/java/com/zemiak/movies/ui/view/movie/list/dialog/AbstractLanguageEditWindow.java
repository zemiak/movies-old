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
    protected ComboBox select;
    
    public AbstractLanguageEditWindow() {
        super();
    }

    @Override
    public void process(List<Movie> movies) {
        if (select.getValue() == null) {
            Notification.show("Please, select a language, first", Notification.Type.WARNING_MESSAGE);
            return;
        }
        
        for (Movie movie: movies) {
            movie.setLanguage((Language) select.getValue());
            getMovieService().save(movie);
        }
        
        fireRefreshEvent();
        close();
    }
    
    protected String caption() {
        return "Language";
    }

    @Override
    protected Component content() {
        FormLayout layout = new FormLayout();
        
        select = new ComboBox(caption());
        select.setWidth("10em");
        select.setNullSelectionAllowed(false);
        select.setPageLength(0);
        
        for (Language entry: getLanguageService().all()) {
            select.addItem(entry);
        }
        
        layout.addComponent(select);
        
        return layout;
    }
    
    abstract protected MovieService getMovieService();
    abstract protected LanguageService getLanguageService();
    abstract protected void fireRefreshEvent();
}
