package com.zemiak.movies.service.ui.admin;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import com.zemiak.movies.service.description.Csfd;
import com.zemiak.movies.service.description.Imdb;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UrlController implements Serializable {
    private List<UrlDTO> items;

    private MovieEditForm form;

    public UrlController() {
        items = null;
    }

    public void setMovieForm(final MovieEditForm form) {
        this.form = form;
    }

    public List<UrlDTO> getItems() {
        reloadItems();
        return Collections.unmodifiableList(items);
    }

    public void reloadItems() {
        items = new ArrayList<>();

        Movie movie = form.getBean();
        if (null != movie) {
            items.addAll(new Csfd().getUrlCandidates(movie.getName()));
            items.addAll(new Imdb().getUrlCandidates(null == movie.getOriginalName()
                    ? movie.getName()
                    : movie.getOriginalName()));
        }
    }
}
