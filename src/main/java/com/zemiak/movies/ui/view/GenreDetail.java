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

        for (Author author: series) {
            NavigationButton button = new NavigationButton(author.getName());
            button.setSizeUndefined();
            grid.addComponent(button);

            final Author finalAuthor = author;

            button.addClickListener(new NavigationButton.NavigationButtonClickListener() {
                @Override
                public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                    authorView.setAuthor(finalAuthor);
                    getNavManager().navigateTo(authorView);
                }
            });
        }
    }

    private void refreshData() {
        series = letter.getAuthors();
    }
}
