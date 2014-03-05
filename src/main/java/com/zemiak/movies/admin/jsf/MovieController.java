package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.admin.jsf.util.JsfUtil.PersistAction;
import com.zemiak.movies.admin.beans.MovieFacade;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Serie;

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
    com.zemiak.movies.admin.beans.MovieFacade ejbFacade;
    Movie selectedOne;
    Movie[] selected;

    Genre genre;
    Serie serie;
    Language language, originalLanguage, subtitles;

    List<Movie> movies = null;

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

    public List<Movie> getItems() {
        if (null == movies) {
            movies = getFacade().findAll();
        }

        return movies;
    }

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

    public List<Movie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Movie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void orderUp() {
        adjustOrder(-1, ResourceBundle.getBundle("/Bundle").getString("OrderAdjusted"));
    }

    public void orderDown() {
        adjustOrder(1, ResourceBundle.getBundle("/Bundle").getString("OrderAdjusted"));
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
            refresh();
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

    public boolean isSelectionEmpty() {
        return selected == null || selected.length == 0;
    }

    public void changeGenre() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setGenreId(genre);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("GenreChanged"));
    }

    public void changeSerie() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setSerieId(serie);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("SerieChanged"));
    }

    public void changeLanguage() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setLanguage(language);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("LanguageChanged"));
    }

    public void changeOriginalLanguage() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setOriginalLanguage(originalLanguage);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("OriginalLanguageChanged"));
    }

    public void changeSubtitles() {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                movie.setSubtitles(subtitles);
                getFacade().edit(movie);
            }
        }, ResourceBundle.getBundle("/Bundle").getString("SubtitlesChanged"));
    }

    private void refresh() {
        movies = null;

        clearSelected();
    }

    public void clearSelected() {
        selected = null;
        selectedOne = null;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Language getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(Language subtitles) {
        this.subtitles = subtitles;
    }
}
