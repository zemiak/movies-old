package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.*;
import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.GenreService;
import com.zemiak.movies.service.LanguageService;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import com.zemiak.movies.service.configuration.Configuration;
import com.zemiak.movies.service.description.DescriptionReader;
import com.zemiak.movies.service.thumbnail.ThumbnailReader;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("movieEditForm")
public class MovieEditForm implements Serializable {
    private Integer id;
    private Movie bean;
    private String selectedUrl;
    private final UrlController urlControl;

    @Inject
    private MovieService service;

    @Inject
    private GenreService genres;

    @Inject
    private SerieService series;

    @Inject
    private LanguageService languages;

    public MovieEditForm() {
        urlControl = new UrlController();
        urlControl.setMovieForm(this);
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

    public List<Language> getLanguages() {
        return languages.all();
    }

    public void setGenreAccordingToSerie(AjaxBehaviorEvent event) {
        if (null == bean.getGenreId() || bean.getGenreId().isEmpty()) {
            if (null != bean.getSerieId() && !bean.getSerieId().isEmpty()) {
                bean.setGenreId(bean.getSerieId().getGenreId());
            }
        }
    }

    public List<UrlDTO> getUrls() {
        return urlControl.getItems();
    }

    public String getSelectedUrl() {
        return selectedUrl;
    }

    public void setSelectedUrl(String selectedUrl) {
        this.selectedUrl = selectedUrl;
    }

    public void changeUrlFromSelection(AjaxBehaviorEvent event) {
        if (null == bean.getUrl() || bean.getUrl().isEmpty()) {
            bean.setUrl(selectedUrl);

            if (null == bean.getDescription() || bean.getDescription().trim().isEmpty()) {
                fetchDescription();
            }
        }
    }

    public void refreshUrlSelection(AjaxBehaviorEvent event) {
        urlControl.reloadItems();
    }

    public void fetchDescription() {
        final DescriptionReader reader = new DescriptionReader();
        final ThumbnailReader thumbnail = new ThumbnailReader(new CDILookup().lookup(Configuration.class));
        final String desc = reader.read(bean);

        if (null != desc && !desc.equals(bean.getDescription())) {
            bean.setDescription(desc);
            thumbnail.process(bean);
        }
    }
}