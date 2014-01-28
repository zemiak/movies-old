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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;

/**
 *
 * @author vasko
 */
@Dependent
public class Mash implements IDescriptionReader {
    public Mash() {
    }

    @Override
    public boolean accepts(final Movie movie) {
        return (null != movie.getSerieId() && Serie.MASH_ID.equals(movie.getSerieId().getId()));
    }

    @Override
    public String getDescription(final Movie movie) {
        Map<Integer, MashDescription> mash;
        
        try {
            mash = readMashDesctiptions();
        } catch (IOException ex) {
            Logger.getLogger(Mash.class.getName()).log(Level.SEVERE, "Cannot read descriptions from file", ex);
            return null;
        }
        
        if (mash.containsKey(movie.getDisplayOrder())) {
            final MashDescription desc = mash.get(movie.getDisplayOrder());
            return desc.getTitle() + ": " + desc.getDescription();
        }
        
        return null;
    }
    
    private Map<Integer, MashDescription> readMashDesctiptions() throws FileNotFoundException, IOException {
        final Map<Integer, MashDescription> map = new HashMap<>();
        final String fileName = VaadinServlet.getCurrent().getServletContext().getRealPath("mash.txt");
        final BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        
        while ((line = br.readLine()) != null) {
            final String[] fields = line.split("|");
            final Integer id = Integer.valueOf(fields[0]);
            final MashDescription desc = new MashDescription(fields[1], fields[2]);
            
            map.put(id, desc);
        }
        
        return map;
    }
}
