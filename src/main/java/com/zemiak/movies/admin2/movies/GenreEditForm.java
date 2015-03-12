package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.service.GenreService;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;

@SessionScoped
@Named("genreEditForm")
public class GenreEditForm implements Serializable {
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

    public void create() {
        bean = Genre.create();
    }

    public String save() {
        service.save(bean);

        return "index";
    }

    public String remove() {
        if (null == bean || null == bean.getId()) {
            throw new NotFoundException();
        }

        service.remove(bean.getId());
        return "index";
    }
}
