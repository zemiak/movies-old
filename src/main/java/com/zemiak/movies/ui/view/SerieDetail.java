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
class SerieDetail extends ViewAbstract {
    CssLayout grid = null;
    
    Serie serie;
    List<Movie> movies;
    
    @Inject MovieDetail movieView;
    
    public SerieDetail() {
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
        refreshData();
    }
    
    @Override
    public void attach() {
        super.attach();
        
        grid = new CssLayout();
        grid.setWidth("100%");
        grid.addStyleName("grid");
        setContent(grid);
        
        new Responsive(grid);
    }
    
    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        setCaption("Serie " + serie.getName());
        
        refresh();
    }

    private void refresh() {
        grid.removeAllComponents();

        for (Movie movie: movies) {
            NavigationButton button = new NavigationButton(movie.getName());
            button.setSizeUndefined();
            grid.addComponent(button);

            final Movie finalMovie = movie;

            button.addClickListener(new NavigationButton.NavigationButtonClickListener() {
                @Override
                public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                    movieView.setMovie(finalMovie);
                    getNavManager().navigateTo(movieView);
                }
            });
        }
    }

    private void refreshData() {
        movies = serie.getMovieList();
    }
}
