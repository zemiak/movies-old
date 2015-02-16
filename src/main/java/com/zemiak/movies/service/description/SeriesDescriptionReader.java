package com.zemiak.movies.service.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SeriesDescriptionReader implements IDescriptionReader {
    final Map<Integer, ManagedSerieInfo> series; // id -> fileName

    public SeriesDescriptionReader() {
        series = new HashMap<>();
        readManagedSeries();
    }

    @Override
    public boolean accepts(final Movie movie) {
        return null != movie.getSerieId()
                && series.keySet().contains(movie.getSerieId().getId())
                && null != movie.getDisplayOrder()
                && series.get(movie.getSerieId().getId()).getRange().contains(movie.getDisplayOrder());
    }

    @Override
    public String getDescription(final Movie movie) {
        String raw;

        try {
            raw = series.get(movie.getSerieId().getId()).getDescriptions().getString(movie.getDisplayOrder().toString());
        } catch (MissingResourceException ex) {
            return null;
        }

        String[] fields = raw.split("\\|");
        return fields[0] + ": " + fields[1];
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

    @Override
    public String getReaderName() {
        return "ManagedSeries";
    }

    private void readManagedSeries() {
        final ResourceBundle seriesBundle = ResourceBundle.getBundle("series");
        final String ids = seriesBundle.getString("managed.id");

        for (String id: ids.split(",")) {
            addOneSerie(seriesBundle, Integer.valueOf(id));
        }
    }

    private void addOneSerie(ResourceBundle seriesBundle, Integer id) {
        ResourceBundle bundle = ResourceBundle.getBundle(seriesBundle.getString("managed.file." + id));

        ManagedSerieInfo serie = new ManagedSerieInfo();
        serie.setId(id);
        serie.setDescriptions(bundle);
        serie.setName(bundle.getString("name"));
        serie.setRange(new HashSet<>());

        String[] rangeNumbers = bundle.getString("range").split(",");
        for (String number: rangeNumbers) {
            serie.getRange().add(Integer.valueOf(number));
        }

        series.put(id, serie);
    }
}
