package com.zemiak.movies.ui.view;

import com.vaadin.addon.responsive.Responsive;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.CssLayout;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.ui.view.components.ButtonIcon;
import com.zemiak.movies.ui.view.components.ImageButton;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class SearchResultsNew extends ViewAbstract {
    public static final int COUNT = 50;
    
    CssLayout grid = null;

    @Inject MovieService movieService;
    @Inject MovieDetail movieView;

    private List<Movie> movies;

    public SearchResultsNew() {
    }
    
    @Override
    protected void onBecomingVisible() {
        movies = movieService.getLastMovies(COUNT);
        
        super.onBecomingVisible();
        setCaption("Last " + COUNT + " Movies");
        
        refresh();
    }

    private void refresh() {
        grid = new CssLayout();
        grid.setWidth("100%");
        grid.addStyleName("grid");
        setContent(grid);

        new Responsive(grid);
        
        if (! movies.isEmpty()) {
            for (Movie movie: movies) {
                NavigationButton button = new ImageButton(movie.getName(), ButtonIcon.movieIcon(movie));
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
