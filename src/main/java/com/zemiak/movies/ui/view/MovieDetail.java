package com.zemiak.movies.ui.view;

import com.vaadin.addon.responsive.Responsive;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.CssLayout;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
class MovieDetail extends ViewAbstract {
    CssLayout grid;
    
    Movie movie;
    
    public MovieDetail() {
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    @Override
    public void attach() {
        super.attach();
        
        grid = new CssLayout();
        grid.setWidth("100%");
        grid.addStyleName("grid");
        setContent(grid);
    }
    
    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        setCaption("Movie " + movie.getName());
        
        refresh();
    }

    private void refresh() {
        grid.removeAllComponents();

        // TODO: render movie details
    }
}
