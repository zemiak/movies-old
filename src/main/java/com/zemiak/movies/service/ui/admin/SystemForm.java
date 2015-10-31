package com.zemiak.movies.service.ui.admin;

import com.zemiak.movies.batch.service.UpdateMoviesScheduler;
import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.service.GenreService;
import com.zemiak.movies.service.LanguageService;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("systemForm")
public class SystemForm implements Serializable {
    @Inject private GenreService genres;
    @Inject private SerieService series;
    @Inject private LanguageService languages;
    @Inject private MovieService movies;
    @Inject private Event<CacheClearEvent> clearCacheEvents;
    @Inject private UpdateMoviesScheduler runner;

    public SystemForm() {
    }

    public Integer getGenres() {
        return genres.all().size();
    }

    public Integer getSeries() {
        return series.all().size();
    }

    public Integer getLanguages() {
        return languages.all().size();
    }

    public Integer getAllMovies() {
        return movies.all().size();
    }

    public Integer getNewMovies() {
        return movies.findAllNew().size();
    }

    public void clearCache() {
        clearCacheEvents.fire(new CacheClearEvent());
        JsfMessages.addSuccessMessage("The cache has been cleared.");
    }

    public void runImport() {
        runner.start();
    }

    public void runBackup() {
        runner.backupAndClean();
    }
}
