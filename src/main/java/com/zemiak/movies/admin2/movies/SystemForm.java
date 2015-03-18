package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.service.*;
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
    @Inject private BatchLogService logs;
    @Inject private MovieService movies;
    @Inject private Event<CacheClearEvent> clearCacheEvents;
    @Inject private BatchRunner runner;

    public SystemForm() {
    }

    public Integer getGenres() {
        return genres.all().size();
    }

    public Integer getSeries() {
        return series.all().size();
    }

    public Integer getLogs() {
        return logs.all().size();
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
        if (runner.isUpdateMoviesRunning()) {
            JsfMessages.addErrorMessage("Job is still running.");
        } else {
            runner.runUpdateCollection();
            JsfMessages.addSuccessMessage("Job has been submitted.");
        }
    }
}
