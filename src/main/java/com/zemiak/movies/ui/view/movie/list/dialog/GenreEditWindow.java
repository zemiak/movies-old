package com.zemiak.movies.ui.view.movie.list.dialog;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.ui.view.movie.list.MovieListRefreshEvent;
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
public class GenreEditWindow extends AbstractEditWindow {
    @Inject private GenreService service;
    @Inject private MovieService movieService;
    @PersistenceContext private EntityManager em;
    @Inject private javax.enterprise.event.Event<MovieListRefreshEvent> events;
    
    protected ComboBox select;
    
    public GenreEditWindow() {
        super();
    }
    
    @Override
    public void process(List<Movie> movies) {
        if (select.getValue() == null) {
            Notification.show("Please, select a genre, first", Notification.Type.WARNING_MESSAGE);
            return;
        }
        
        for (Movie movie: movies) {
            movie.setGenreId((Genre) select.getValue());
            movieService.save(movie);
        }
        
        events.fire(new MovieListRefreshEvent());
        close();
    }
    
    @Override
    protected Component content() {
        FormLayout layout = new FormLayout();
        
        select = new ComboBox("Genre");
        select.setWidth("10em");
        select.setNullSelectionAllowed(false);
        select.setPageLength(0);
        
        for (Genre entry: service.all()) {
            select.addItem(entry);
        }
        
        layout.addComponent(select);
        
        return layout;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
