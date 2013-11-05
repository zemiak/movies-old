package com.zemiak.movies.ui.admin.view.movie.list;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Serie;
import java.util.List;
import org.tepi.filtertable.FilterGenerator;

/**
 *
 * @author vasko
 */
public class CustomFilterGenerator implements FilterGenerator {
    private List<Genre> genres;
    private List<Serie> series;
    
    public CustomFilterGenerator(List<Genre> genres, List<Serie> series) {
        this.genres = genres;
        this.series = series;
    }

    @Override
    public Container.Filter generateFilter(Object propertyId, Object value) {
        return null;
    }

    @Override
    public Container.Filter generateFilter(Object propertyId, Field<?> originatingField) {
        return null;
    }

    @Override
    public AbstractField<?> getCustomFilterComponent(Object propertyId) {
        if ("Genre".equals(propertyId)) {
            return generateGenreFilter();
        } else if ("Serie".equals(propertyId)) {
            return generateSerieFilter();
        }
        
        return null;
    }

    @Override
    public void filterRemoved(Object propertyId) {
    }

    @Override
    public void filterAdded(Object propertyId, Class<? extends Container.Filter> filterType, Object value) {
    }

    @Override
    public Container.Filter filterGeneratorFailed(Exception reason, Object propertyId, Object value) {
        return null;
    }

    private AbstractField<?> generateGenreFilter() {
        ComboBox pageFilter = new ComboBox();
        pageFilter.setPageLength(0);
        
        Object allItem = pageFilter.addItem();
        pageFilter.setNullSelectionItemId(allItem);
        pageFilter.setItemCaption(allItem, "(All)");
        
        for (Genre genre: genres) {
            pageFilter.addItem(genre);
            pageFilter.setItemCaption(genre, genre.getName());
        }
        
        return pageFilter;
    }

    private AbstractField<?> generateSerieFilter() {
        ComboBox pageFilter = new ComboBox();
        pageFilter.setPageLength(20);
        
        Object allItem = pageFilter.addItem();
        pageFilter.setNullSelectionItemId(allItem);
        pageFilter.setItemCaption(allItem, "(All)");
        
        for (Serie serie: series) {
            pageFilter.addItem(serie);
            pageFilter.setItemCaption(serie, serie.getName());
        }
        
        return pageFilter;
    }
    
}
