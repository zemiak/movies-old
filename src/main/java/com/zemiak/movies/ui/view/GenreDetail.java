package com.zemiak.movies.ui.view;

import com.vaadin.addon.responsive.Responsive;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.CssLayout;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.ui.view.icon.ButtonIcon;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
class GenreDetail extends ViewAbstract {
    CssLayout grid = null;
    
    Genre genre;
    List<Serie> series;
    List<Movie> movies;
    
    @Inject SerieDetail serieView;
    @Inject MovieDetail movieView;
    
    public GenreDetail() {
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
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
        setCaption("Genre " + genre.getName());
        
        refresh();
    }

    private void refresh() {
        grid.removeAllComponents();

        for (Serie serie: series) {
            NavigationButton button = new NavigationButton(serie.getName());
            button.setSizeUndefined();
            button.setIcon(ButtonIcon.serieIcon(serie));
            grid.addComponent(button);

            final Serie finalSerie = serie;

            button.addClickListener(new NavigationButton.NavigationButtonClickListener() {
                @Override
                public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                    serieView.setSerie(finalSerie);
                    getNavManager().navigateTo(serieView);
                }
            });
        }
        
        for (Movie movie: movies) {
            NavigationButton button = new NavigationButton(movie.getName());
            button.setSizeUndefined();
            button.setIcon(ButtonIcon.movieIcon(movie));
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
        series = genre.getSerieList();
        movies = genre.getMovieList();
    }
}
