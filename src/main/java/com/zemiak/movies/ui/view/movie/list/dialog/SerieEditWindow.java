package com.zemiak.movies.ui.view.movie.list.dialog;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.boundary.SerieService;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Dependent
public class SerieEditWindow extends AbstractEditWindow {
    @Inject private SerieService service;
    
    protected ComboBox select;
    
    public SerieEditWindow() {
        super();
    }
    
    @Override
    public void process(List<Movie> movies) {
        if (select.getValue() == null) {
            Notification.show("Please, select a serie, first", Notification.Type.WARNING_MESSAGE);
            return;
        }
        
        for (Movie movie: movies) {
            movie.setSerieId((Serie) select.getValue());
            getMovieService().save(movie);
        }
    }
    
    @Override
    protected Component content() {
        FormLayout layout = new FormLayout();
        
        select = new ComboBox("Serie");
        select.setWidth("10em");
        
        for (Serie entry: service.all()) {
            select.addItem(entry);
        }
        
        layout.addComponent(select);
        
        return layout;
    }

    @PersistenceContext private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Inject private MovieService movieService;
    protected MovieService getMovieService() {
        return movieService;
    }
}
