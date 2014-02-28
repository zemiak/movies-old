package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.admin.jsf.util.JsfUtil.PersistAction;
import com.zemiak.movies.admin.beans.MovieFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named("movieController")
@SessionScoped
public class MovieController implements Serializable {

    @EJB
    private com.zemiak.movies.admin.beans.MovieFacade ejbFacade;
    private Movie selectedOne;
    private Movie[] selected;

    public MovieController() {
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

    private MovieFacade getFacade() {
        return ejbFacade;
    }

    public Movie prepareCreate() {
        selectedOne = new Movie();
        initializeEmbeddableKey();
        return selectedOne;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MovieCreated"));
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MovieUpdated"));
    }

    public void destroy() {
        if (isSelectionEmpty()) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("SelectAtLeastOne"));
            return;
        }

        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MovieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedOne = null; // Remove selection
            selected = null;
        }
    }

    public List<Movie> getItems() {
        return getFacade().findAll();
    }

    public List<Movie> getNewItems() {
        return getFacade().findAllNew();
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selectedOne != null || (selected.length > 0 && persistAction == PersistAction.DELETE)) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selectedOne);
                } else {
                    if (selectedOne != null) {
                        getFacade().remove(selectedOne);
                    } else {
                        for (Movie movie: selected) {
                            getFacade().remove(movie);
                        }
                    }
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

    public List<Movie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Movie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void orderUp() {
        adjustOrder(-1, ResourceBundle.getBundle("/Bundle").getString("OrderAdjusted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedOne = null; // Remove selection
            selected = null;
        }
    }

    public void orderDown() {
        adjustOrder(1, ResourceBundle.getBundle("/Bundle").getString("OrderAdjusted"));
        if (!JsfUtil.isValidationFailed()) {
            selectedOne = null; // Remove selection
            selected = null;
        }
    }

    private void adjustOrder(final int offset, String successMessage) {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                Integer order = movie.getDisplayOrder() == null ? 2 : movie.getDisplayOrder();

                if (order + offset > 0) {
                    movie.setDisplayOrder(order + offset);
                    getFacade().edit(movie);
                }
            }
        }, successMessage);
    }

    private void changeMultipleItems(final ChangeMovieItem changer, final String successMessage) {
        setEmbeddableKeys();
        try {
            for (Movie movie: selected) {
                changer.change(movie, ejbFacade);
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

    private boolean isSelectionEmpty() {
        return selected == null || selected.length == 0;
    }

    public void changeGenre() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setDisplayOrder(-999);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("GenreChanged"));
    }

    public void changeSerie() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setDisplayOrder(-999);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("SerieChanged"));
    }

    public void changeLanguage() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setDisplayOrder(-999);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("LanguageChanged"));
    }

    public void changeOriginalLanguage() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setDisplayOrder(-999);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("OriginalLanguageChanged"));
    }

    public void changeSubtitles() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setDisplayOrder(-999);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("SubtitlesChanged"));
    }
}
