package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.description.Csfd;
import com.zemiak.movies.description.Imdb;
import com.zemiak.movies.domain.UrlDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("urlController")
@SessionScoped
public class UrlController implements Serializable {
    private List<UrlDTO> items;
    private UrlDTO selected;
    
    public UrlController() {
        items = new ArrayList<>();
        selected = null;
    }
    
    public void prepareUrls(final String movieName) {
        items = new ArrayList<>();
        items.addAll(new Csfd().getUrlCandidates(movieName));
        items.addAll(new Imdb().getUrlCandidates(movieName));
    }
    
    public List<UrlDTO> getItems() {
        return items;
    }

    public void setItems(final List<UrlDTO> urls) {
        this.items = urls;
    }

    public UrlDTO getSelected() {
        return selected;
    }

    public void setSelected(final UrlDTO url) {
        this.selected = url;
    }
}
