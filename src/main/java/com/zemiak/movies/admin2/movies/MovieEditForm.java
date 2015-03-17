package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.service.GenreService;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("movieEditForm")
public class MovieEditForm implements Serializable {
    private Integer id;
    private Movie bean;

    @Inject
    private MovieService service;

    @Inject
    private GenreService genres;

    @Inject
    private SerieService series;

    public MovieEditForm() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;

        if (-1 == id) {
            create();
        } else {
            refresh();
        }
    }

    private void refresh() {
        bean = service.find(id);

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find movie #" + id);
        }
    }

    public Movie getBean() {
        return bean;
    }

    private void create() {
        bean = Movie.create();
    }

    public String save() {
        service.save(bean);
        JsfMessages.addSuccessMessage("The movie has been saved");

        return close();
    }

    public String close() {
        return "index";
    }

    public String remove() {
        try {
            service.remove(id);
            JsfMessages.addSuccessMessage("The movie has been deleted");
        } catch (EJBException ex) {
            JsfMessages.addErrorMessage(ex);
        }

        return close();
    }

    public String getActionTitle() {
        return isNew() ? "Create" : "Edit";
    }

    public boolean isNew() {
        return null == id || -1 == id;
    }

    public List<Genre> getGenres() {
        return genres.all();
    }

    public void setSerie(Serie serie) {
        bean.setSerieId(serie);

        if (null == bean.getGenreId() || bean.getGenreId().isEmpty()) {
            bean.setGenreId(serie.getGenreId());
        }
    }

    public List<Serie> getSeries() {
        if (null == bean.getGenreId() || bean.getGenreId().isEmpty()) {
            return series.all();
        }

        return bean.getGenreId().getSerieList();
    }
}
