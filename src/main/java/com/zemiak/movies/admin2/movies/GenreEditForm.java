package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.service.GenreService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;

@RequestScoped
@Named
public class GenreEditForm {
    private Integer id;
    private Genre bean;

    @Inject
    private GenreService service;

    public GenreEditForm() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;

        refresh();
    }

    private void refresh() {
        bean = service.find(id);

        if (null == bean) {
            throw new NotFoundException();
        }
    }

    public Genre getBean() {
        return bean;
    }
}
