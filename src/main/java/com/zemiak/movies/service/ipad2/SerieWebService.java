package com.zemiak.movies.service.ipad2;

import com.zemiak.movies.admin2.movies.JsfMessages;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.service.SerieService;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("serieWebService")
public class SerieWebService implements Serializable {
    @Inject private SerieService service;
    @Inject private GenreWebService genreWebService;

    private Integer id;
    private Serie bean;

    public List<Serie> getByGenreId() {
        return service
                .all()
                .stream()
                .filter(serie -> genreWebService.getId().equals(serie.getGenreId().getId()))
                .sorted()
                .collect(Collectors.toList());
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
            JsfMessages.addErrorMessage("Serie #" + id + " cannot be found");
            return "index";
        }

        return null;
    }

    public Serie getBean() {
        return bean;
    }
}
