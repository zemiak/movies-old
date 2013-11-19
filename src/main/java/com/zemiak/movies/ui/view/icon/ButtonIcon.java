package com.zemiak.movies.ui.view.icon;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;

/**
 *
 * @author vasko
 */
public class ButtonIcon {
    final static private String BASE_URL = "http://lenovo-server.local/movies/img";
    
    public static Resource genreIcon(Genre genre) {
        return getIcon("genre", genre.getPictureFileName());
    }
    
    public static Resource serieIcon(Serie serie) {
        return getIcon("serie", serie.getPictureFileName());
    }
    
    public static Resource movieIcon(Movie movie) {
        return getIcon("movie", movie.getPictureFileName());
    }

    private static Resource getIcon(String category, String pictureFileName) {
        return new ExternalResource(BASE_URL + "/" + category + "/" + pictureFileName);
    }
}
