package com.zemiak.movies.ui.view;

import com.vaadin.addon.responsive.Responsive;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.boundary.SerieService;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class SearchResults extends ViewAbstract {
    CssLayout grid = null;

    String text;
    
    @Inject MovieService movieService;
    @Inject GenreService genreService;
    @Inject SerieService serieService;
    
    @Inject MovieDetail movieView;
    @Inject GenreDetail genreView;
    @Inject SerieDetail serieView;

    private List<Movie> movies;
    private List<Genre> genres;
    private List<Serie> series;

    public SearchResults() {
    }
    
    public void setText(String text) {
        this.text = text;
        refreshData();
    }
    
    private void refreshData() {
        movies = movieService.getByExpression(this.text);
        genres = genreService.getByExpression(this.text);
        series = serieService.getByExpression(this.text);
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        setCaption("?" + text);
        
        refresh();
    }

    private String getHighlightedValue(String value) {
        String highlighted = "";
        int i = value.toLowerCase().indexOf(text);

        if (i > 0) {
            highlighted = value.substring(0, i);
        }

        // Gold color
        highlighted += "<span style=\"background-color: #FFD700;\">"
                + value.substring(i, i + text.length())
                + "</span>";

        if (value.length() > i + text.length()) {
            highlighted += value.substring(i + text.length(), value.length());
        }

        return highlighted;
    }

    private void refresh() {
        grid = new CssLayout();
        grid.setWidth("100%");
        grid.addStyleName("grid");
        setContent(grid);

        if (text.isEmpty()) {
            grid.addComponent(new Label("Cannot search for an empty string"));
            return;
        }
        
        if (movies.isEmpty() && genres.isEmpty() && series.isEmpty()) {
            Label searchText = new Label("No results for text: <b>" + text + "</b>");
            searchText.setContentMode(ContentMode.HTML);
            grid.addComponent(searchText);
            return;
        }
        
        new Responsive(grid);
        
        if (! genres.isEmpty()) {
            for (Genre genre: genres) {
                NavigationButton button = new NavigationButton();
                button.setSizeUndefined();
                button.setDescription(getHighlightedValue(genre.getName()));
                grid.addComponent(button);

                final Genre finalGenre = genre;

                button.addClickListener(new NavigationButton.NavigationButtonClickListener() {
                    @Override
                    public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                        genreView.setGenre(finalGenre);
                        getNavManager().navigateTo(genreView);
                    }
                });
            }
        }

        if (! series.isEmpty()) {
            for (Serie serie: series) {
                NavigationButton button = new NavigationButton();
                button.setSizeUndefined();
                button.setDescription(getHighlightedValue(serie.getName()));
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
        }
        
        if (! movies.isEmpty()) {
            for (Movie movie: movies) {
                NavigationButton button = new NavigationButton();
                button.setSizeUndefined();
                button.setDescription(getHighlightedValue(movie.getName()));
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
    }
}
