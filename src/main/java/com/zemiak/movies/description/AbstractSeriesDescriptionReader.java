package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

abstract class AbstractSeriesDescriptionReader implements IDescriptionReader {
    Map<Integer, SerieItemDescription> descriptions;
    
    abstract protected Integer getSerieId();
    abstract protected String getBundleName();
    abstract protected List<Integer> getRange();
    
    @Override
    abstract public String getReaderName();

    public AbstractSeriesDescriptionReader() {
        descriptions = readDescriptions();
    }

    @Override
    public boolean accepts(final Movie movie) {
        return null != movie.getSerieId() 
                && getSerieId().equals(movie.getSerieId().getId())
                && null != movie.getDisplayOrder()
                && getRange().contains(movie.getDisplayOrder());
    }

    @Override
    public String getDescription(final Movie movie) {
        if (descriptions.containsKey(movie.getDisplayOrder())) {
            final SerieItemDescription desc1 = descriptions.get(movie.getDisplayOrder());
            return desc1.getTitle() + ": " + desc1.getDescription();
        }

        return null;
    }

    private Map<Integer, SerieItemDescription> readDescriptions() {
        final Map<Integer, SerieItemDescription> map = new HashMap<>();
        final ResourceBundle bundle = ResourceBundle.getBundle(getBundleName());

        for (int i: getRange()) {
            final String line = bundle.getString(String.valueOf(i));
            final String[] fields = line.split("\\|");
            final SerieItemDescription desc = new SerieItemDescription(fields[0], fields[1]);

            map.put(i, desc);
        }

        return map;
    }

    @Override
    public List<UrlDTO> getUrlCandidates(String movieName) {
        return new ArrayList<>();
    }
    
    protected void addAll(final List<Integer> target, final Integer startInclusive, final Integer stopExclusive) {
        for (int order = startInclusive; order < stopExclusive; order++) {
            target.add(order);
        }
    }
}
