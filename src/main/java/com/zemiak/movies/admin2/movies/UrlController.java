package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import com.zemiak.movies.service.description.Csfd;
import com.zemiak.movies.service.description.Imdb;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UrlController implements Serializable {
    private List<UrlDTO> items;

    private Movie forMovie;
    private MovieEditForm form;

    public UrlController() {
        items = null;
    }

    public void setMovieForm(final MovieEditForm form) {
        this.form = form;
    }

    public List<UrlDTO> getItems() {
        reloadItems();
        return items;
    }

    public void reloadItems() {
        if (null != items) {
            if (null == forMovie) {
                items = null;
                forMovie = form.getBean();
            } else {
                if (null != form.getBean()) {
                    if (!forMovie.getId().equals(form.getBean().getId())) {
                        items = null;
                        forMovie = form.getBean();
                    }
                } else {
                    forMovie = null;
                }
            }
        } else {
            if (null != form.getBean()) {
                forMovie = form.getBean();
            }
        }

        if (null == items && null != forMovie) {
            items = new ArrayList<>();
            items.addAll(new Csfd().getUrlCandidates(form.getBean().getName()));
            items.addAll(new Imdb().getUrlCandidates(null == form.getBean().getOriginalName()
                    ? form.getBean().getName()
                    : form.getBean().getOriginalName()));
        }
    }
}
