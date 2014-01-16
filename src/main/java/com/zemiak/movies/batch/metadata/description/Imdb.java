package com.zemiak.movies.batch.metadata.description;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author vasko
 */
@Dependent
public class Imdb implements IDescriptionReader {
    private static final String URL1 = "www.imdb.com/";
    private static final String URL2 = "http://" + URL1;
    
    public Imdb() {
    }

    @Override
    public boolean acceptsUrl(final String url) {
        return url.startsWith(URL1) || url.startsWith(URL2);
    }

    @Override
    public String getDescription(final String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Logger.getLogger(Imdb.class.getName()).log(Level.SEVERE, "Cannot read " + url, ex);
            return null;
        }
        
        Element description = doc.select("meta[name=description]").first();
        return description.attr("description");
    }
}
