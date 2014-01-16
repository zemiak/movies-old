package com.zemiak.movies.batch.metadata.description;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 *
 * @author vasko
 */
@Dependent
public class Csfd implements IDescriptionReader {
    private static final String URL1 = "www.csfd.cz/";
    private static final String URL2 = "http://" + URL1;
    
    public Csfd() {
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
            Logger.getLogger(Csfd.class.getName()).log(Level.SEVERE, "Cannot read " + url, ex);
            return null;
        }
        
        Element description = doc.select("div[data-truncate]").first();
        
        return description.text();
    }
}
