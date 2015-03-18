package com.zemiak.movies.service.ipad2;

import com.zemiak.movies.admin2.movies.JsfMessages;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.GenreDTO;
import com.zemiak.movies.service.GenreService;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("genreWebService")
@SessionScoped
public class GenreWebService implements Serializable {
    @Inject private GenreService service;
    private Integer id;
    private Genre bean;

    public List<Genre> getAll() {
        return service.all();
    }

    public GenreDTO find(String id) {
        return new GenreDTO(service.find(Integer.valueOf(id)));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String check() {
        if (null != id) {
            bean = service.find(id);
        }

        if (null == id || null == bean) {
            JsfMessages.addErrorMessage("Genre #" + id + " cannot be found");
            return "index";
        }

        return null;
    }

    public Genre getBean() {
        return bean;
    }
}
