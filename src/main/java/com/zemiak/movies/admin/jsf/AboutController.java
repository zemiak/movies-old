package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.service.BatchRunner;
import com.zemiak.movies.service.GenreService;
import com.zemiak.movies.service.LanguageService;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.SerieService;
import com.zemiak.movies.domain.AboutItem;
import com.zemiak.movies.domain.CacheClearEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("aboutController")
@SessionScoped
public class AboutController implements Serializable {
    private static final Logger LOG = Logger.getLogger(AboutController.class.getName());

    @Inject GenreService genres;
    @Inject SerieService series;
    @Inject LanguageService languages;
    @Inject MovieService movies;
    @Inject Event<CacheClearEvent> clearCacheEvents;
    @Inject BatchRunner runner;

    private List<AboutItem> items;

    public AboutController() {
        items = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        items.add(new AboutItem(1, "Languages", String.valueOf(languages.all().size())));
        items.add(new AboutItem(2, "Genres", String.valueOf(genres.all().size())));
        items.add(new AboutItem(3, "Series", String.valueOf(series.all().size())));
        items.add(new AboutItem(4, "Movies", String.valueOf(movies.all().size())));
        items.add(new AboutItem(5, "New Movies", String.valueOf(movies.findAllNew().size())));

    }

    public List<AboutItem> getItems() {
        return items;
    }

    public void clearCache() {
        clearCacheEvents.fire(new CacheClearEvent());
        showMessage("Data Cache Cleared");
    }

    public void runImport() {
        if (runner.isUpdateMoviesRunning()) {
            showMessage("Job is still running");
        } else {
            runner.runUpdateCollection();
            showMessage(String.format("Submitted a job with id %d", runner.getUpdateMoviesJob()));
        }
    }

    private void showMessage(final String message) {
        JsfUtil.addSuccessMessage(message);
    }

    public AboutItem getAboutItem(final Integer id) {
        return items.get(id - 1);
    }
    
    public boolean isBatchRunning() {
        return runner.isUpdateMoviesRunning();
    }
    
    public Long getBatchJobNumber() {
        return runner.getUpdateMoviesJob();
    }

    @FacesConverter(forClass = AboutItem.class)
    public static class AboutItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AboutController controller = (AboutController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aboutController");
            return controller.getAboutItem(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AboutItem) {
                AboutItem o = (AboutItem) object;
                return getStringKey(o.getId());
            } else {
                LOG.log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AboutItem.class.getName()});
                return null;
            }
        }

    }
}
