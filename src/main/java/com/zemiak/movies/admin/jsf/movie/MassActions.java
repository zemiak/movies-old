package com.zemiak.movies.admin.jsf.movie;

import com.zemiak.movies.admin.beans.MovieFacade;
import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.description.DescriptionReader;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;

/**
 *
 * @author vasko
 */
public class MassActions {
    private final AbstractMovieController controller;
    Genre genre;
    Serie serie;
    Language language, originalLanguage, subtitles;
    
    public MassActions(final AbstractMovieController controller) {
        this.controller = controller;
    }
    
    public void orderUp() {
        adjustOrder(-1, ResourceBundle.getBundle("/Bundle").getString("OrderAdjusted"));
    }

    public void orderDown() {
        adjustOrder(1, ResourceBundle.getBundle("/Bundle").getString("OrderAdjusted"));
    }
    
    private MovieFacade getFacade() {
        return controller.getFacade();
    }

    private void adjustOrder(final int offset, String successMessage) {
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                Integer order = movie.getDisplayOrder() == null ? 2 : movie.getDisplayOrder();
                
                movie.setDisplayOrder((order + offset > 0) ? (order + offset) : 1);
                getFacade().edit(movie);
            }
        }, successMessage);
    }

    private void changeMultipleItems(final ChangeMovieItem changer, final String successMessage) {
        controller.setEmbeddableKeys();
        try {
            for (Movie movie: controller.selected) {
                changer.change(movie, controller.ejbFacade);
            }

            JsfUtil.addSuccessMessage(successMessage);
            controller.refresh();
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
                
                if (null == movie.getGenreId() || movie.getGenreId().isEmpty()) {
                    movie.setGenreId(serie.getGenreId());
                }
                
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
    
    public void fetchDescriptions() {
        final DescriptionReader reader = new DescriptionReader();
        
        changeMultipleItems(new ChangeMovieItem() {
            @Override
            public void change(final Movie movie, final MovieFacade facade) {
                final String desc = reader.read(movie);
                
                if (null != desc && !desc.equals(movie.getDescription())) {
                    movie.setDescription(desc);
                    getFacade().edit(movie);
                }
            }
        }, ResourceBundle.getBundle("/Bundle").getString("DescriptionsFetched"));
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
