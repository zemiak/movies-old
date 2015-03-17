package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.service.GenreService;
import java.io.Serializable;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

        if (-1 == id) {
            create();
        } else {
            refresh();
        }
    }

    private void refresh() {
        bean = service.find(id);

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find genre #" + id);
        }
    }

    public Genre getBean() {
        return bean;
    }

    private void create() {
        bean = Genre.create();
    }

    public String save() {
        service.save(bean);
        JsfMessages.addSuccessMessage("The genre has been saved");

        return close();
    }

    public String close() {
        return "index";
    }

    public String remove() {
        try {
            service.remove(id);
            JsfMessages.addSuccessMessage("The genre has been deleted");
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
}
