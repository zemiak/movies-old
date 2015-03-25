package com.zemiak.movies.service.ui.admin;

import com.zemiak.movies.domain.*;
import com.zemiak.movies.service.GenreService;
import com.zemiak.movies.service.LanguageService;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
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
    private final UrlController urlControl;
    private String selectedUrl;
    private String languageId, originalLanguageId, subtitlesId;
    private Integer serieId, genreId;

    @Inject
    private String imgPath;

    @Inject
    private MovieService service;

    @Inject
    private GenreService genres;

    @Inject
    private SerieService series;

    @Inject
    private LanguageService languages;

    @Inject
    private String ffmpeg;

    public MovieEditForm() {
        urlControl = new UrlController();
        urlControl.setMovieForm(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String check() {
        bean = isNew() ? Movie.create() : service.find(id);

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find movie #" + id);
            return close();
        }

        serieId = (null == bean.getSerieId()) ? 0 : bean.getSerieId().getId();
        genreId = (null == bean.getGenreId()) ? 0 : bean.getGenreId().getId();
        languageId = (null == bean.getLanguage()) ? "  " : bean.getLanguage().getId();
        subtitlesId = (null == bean.getSubtitles()) ? "  " : bean.getSubtitles().getId();
        originalLanguageId = (null == bean.getOriginalLanguage()) ? "" : bean.getOriginalLanguage().getId();

        return null;
    }

    public Movie getBean() {
        return bean;
    }

    public String getSelectedUrl() {
        return selectedUrl;
    }

    public void setSelectedUrl(String selectedUrl) {
        this.selectedUrl = selectedUrl;
    }

    public String save() {
        service.save(bean, genreId, serieId, languageId, originalLanguageId, subtitlesId);
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
        return series.all();
    }

    public List<Language> getLanguages() {
        return languages.all();
    }

    public void setGenreAccordingToSerie(AjaxBehaviorEvent event) {
        Genre genre = series.find(serieId).getGenreId();
        if (!genre.getId().equals(genreId)) {
            genreId = genre.getId();
        }
    }

    public List<UrlDTO> getUrls() {
        return urlControl.getItems();
    }

    public void changeUrlFromSelection(AjaxBehaviorEvent event) {
        if (null == bean.getUrl() || bean.getUrl().isEmpty()) {
            bean.setUrl(selectedUrl);

            fetchDescription();
            fetchPicture();
        }
    }

    public Integer getSerieId() {
        return serieId;
    }

    public void setSerieId(Integer serieId) {
        this.serieId = serieId;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public void refreshUrlSelection(AjaxBehaviorEvent event) {
        urlControl.reloadItems();
    }

    private void fetchDescription() {
        final DescriptionReader reader = new DescriptionReader();

        final String desc = reader.read(bean);

        bean.setDescription(desc);
    }

    private void fetchPicture() {
        final ThumbnailReader thumbnail = new ThumbnailReader(imgPath, ffmpeg);
        thumbnail.process(bean);
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getOriginalLanguageId() {
        return originalLanguageId;
    }

    public void setOriginalLanguageId(String originalLanguageId) {
        this.originalLanguageId = originalLanguageId;
    }

    public String getSubtitlesId() {
        return subtitlesId;
    }

    public void setSubtitlesId(String subtitlesId) {
        this.subtitlesId = subtitlesId;
    }


}
