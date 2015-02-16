package com.zemiak.movies.admin.jsf.movie;

import com.zemiak.movies.service.description.Csfd;
import com.zemiak.movies.service.description.Imdb;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;

/**
 *
 * @author vasko
 */
@Dependent
public class UrlController implements Serializable {
    private List<UrlDTO> items;
    private UrlDTO selected;

    private Movie forMovie;
    private AbstractMovieController movieController;
    
    public UrlController() {
        items = null;
        selected = null;
    }
    
    public void setMovieController(final AbstractMovieController movieController) {
        this.movieController = movieController;
    }
    
    public List<UrlDTO> getItems() {
        reloadItems();
        return items;
    }

    public UrlDTO getSelected() {
        return selected;
    }

    public void setSelected(final UrlDTO url) {
        this.selected = url;
    }

    public void reloadItems() {
        if (null != items) {
            if (null == forMovie) {
                items = null;
                forMovie = movieController.getSelectedOne();
            } else {
                if (null != movieController.getSelectedOne()) {
                    if (!forMovie.getId().equals(movieController.getSelectedOne().getId())) {
                        items = null;
                        forMovie = movieController.getSelectedOne();
                    }
                } else {
                    forMovie = null;
                }
            }
        } else {
            if (null != movieController.getSelectedOne()) {
                forMovie = movieController.getSelectedOne();
            }
        }
        
        if (null == items && null != forMovie) {
            items = new ArrayList<>();
            items.addAll(new Csfd().getUrlCandidates(movieController.getSelectedOne().getName()));
            items.addAll(new Imdb().getUrlCandidates(null == movieController.getSelectedOne().getOriginalName() 
                    ? movieController.getSelectedOne().getName() 
                    : movieController.getSelectedOne().getOriginalName()));
            selected = null;
        }
    }
}
