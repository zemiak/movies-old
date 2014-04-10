package com.zemiak.movies.admin.jsf.movie;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.admin.jsf.util.JsfUtil.PersistAction;
import com.zemiak.movies.admin.beans.MovieFacade;
import com.zemiak.movies.description.DescriptionReader;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.inject.Inject;

abstract public class AbstractMovieController implements Serializable {

    @Inject
    com.zemiak.movies.admin.beans.MovieFacade ejbFacade;
    Movie selectedOne;
    Movie[] selected;
    MassActions mass;

    @Inject UrlController urls;

    List<Movie> movies = null;

    public AbstractMovieController() {
        mass = new MassActions(this);
    }

    public Movie getSelectedOne() {
        return selectedOne;
    }

    public Movie[] getSelected() {
        return selected;
    }

    public void setSelected(Movie[] selected) {
        this.selected = selected;

        this.selectedOne = selected.length == 1 ? selected[0] : null;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    protected MovieFacade getFacade() {
        return ejbFacade;
    }

    public Movie prepareCreate() {
        selectedOne = new Movie();
        initializeEmbeddableKey();
        return selectedOne;
    }

    public void create() {
        selectedOne.setId(null);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MovieCreated"));
        refresh();
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MovieUpdated"));
        refresh();
    }

    public void destroy() {
        if (isSelectionEmpty()) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SelectAtLeastOne"));
            return;
        }

        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MovieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            refresh();
        }
    }

    abstract public List<Movie> getItems();

    private void persist(PersistAction persistAction, String successMessage) {
        if (selectedOne != null || (selected.length > 0 && persistAction == PersistAction.DELETE)) {
            setEmbeddableKeys();
            try {
                switch (persistAction) {
                    case CREATE:
                        getFacade().create(selectedOne);
                        break;
                    case UPDATE:
                        getFacade().edit(selectedOne);
                        break;
                    case DELETE:
                        if (selected != null && selected.length > 0) {
                            for (Movie movie: selected) {
                                getFacade().remove(movie);
                            }
                        } else if (selectedOne != null) {
                            getFacade().remove(selectedOne);
                        }
                        break;
                }
                        
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Movie getMovie(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public boolean isSelectionEmpty() {
        return selected == null || selected.length == 0;
    }
    
    public void fetchDescription() {
        final DescriptionReader reader = new DescriptionReader();
        final String desc = reader.read(selectedOne);
                
        if (null != desc && !desc.equals(selectedOne.getDescription())) {
            selectedOne.setDescription(desc);
        }
    }

    void refresh() {
        movies = null;

        clearSelected();
    }

    public void clearSelected() {
        selected = null;
        selectedOne = null;
    }

    public void changeUrl() {
        selectedOne.setUrl(urls.getSelected().getUrl());
        
        if (null == selectedOne.getDescription() || selectedOne.getDescription().trim().isEmpty()) {
            fetchDescription();
        }
    }

    public UrlController getUrls() {
        return urls;
    }

    public void setUrls(UrlController urls) {
        this.urls = urls;
    }
    
    public MassActions getMass() {
        return mass;
    }
}
