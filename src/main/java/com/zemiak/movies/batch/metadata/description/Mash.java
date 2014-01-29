package com.zemiak.movies.batch.metadata.description;

import com.vaadin.server.VaadinServlet;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;

/**
 *
 * @author vasko
 */
@Dependent
public class Mash implements IDescriptionReader {
    Map<Integer, MashDescription> descriptions;

    public Mash() {
        descriptions = readMashDesctiptions();
    }

    @Override
    public boolean accepts(final Movie movie) {
        return (null != movie.getSerieId() && Serie.MASH_ID.equals(movie.getSerieId().getId()));
    }

    @Override
    public String getDescription(final Movie movie) {
        if (descriptions.containsKey(movie.getDisplayOrder())) {
            final MashDescription desc1 = descriptions.get(movie.getDisplayOrder());
            return desc1.getTitle() + ": " + desc1.getDescription();
        }

        return null;
    }

    private Map<Integer, MashDescription> readMashDesctiptions() {
        final Map<Integer, MashDescription> map = new HashMap<>();
        final ResourceBundle bundle = ResourceBundle.getBundle("mash");

        for (int i = 1; i <= 256; i++) {
            final String line = bundle.getString(String.valueOf(i));
            final String[] fields = line.split("\\|");
            final MashDescription desc = new MashDescription(fields[0], fields[1]);

            map.put(i, desc);
        }

        return map;
    }
}
