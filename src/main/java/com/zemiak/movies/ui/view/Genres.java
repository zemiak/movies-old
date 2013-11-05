package com.zemiak.movies.ui.view;

import com.vaadin.addon.responsive.Responsive;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.cdi.CDIView;
import com.vaadin.ui.CssLayout;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.domain.Genre;
import java.util.List;
import javax.inject.Inject;

@CDIView("genres")
public class Genres extends ViewAbstract {
    CssLayout grid = null;
    List<Genre> genres;
    
    @Inject GenreDetail genreView;
    @Inject GenreService genreService;
    
    boolean initialized = false;

    public Genres() {
    }
    
    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        setCaption("Genres");
        
        if (initialized) {
            return;
        }
        
        this.genres = genreService.all();
        
        refresh();
        initialized = true;
    }

    private void refresh() {
        grid = new CssLayout();
        grid.setWidth("100%");
        grid.addStyleName("grid");
        setContent(grid);

        for (Genre genre: genres) {
            NavigationButton button = new NavigationButton(genre.getName());
            button.setSizeUndefined();
            grid.addComponent(button);

            final Genre finalGenre = genre;

            button.addClickListener(new NavigationButton.NavigationButtonClickListener() {
                @Override
                public void buttonClick(NavigationButton.NavigationButtonClickEvent event) {
                    genreView.setGenre(finalGenre);
                    getNavManager().navigateTo(genreView);
                }
            });
            
            grid.addComponent(button);
        }
        
        new Responsive(grid);
    }
}
