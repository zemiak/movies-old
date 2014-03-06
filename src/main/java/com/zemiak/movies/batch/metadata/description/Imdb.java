package com.zemiak.movies.batch.metadata.description;

import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author vasko
 */
public class Imdb implements IDescriptionReader {
    private static final String URL1 = "www.imdb.com/";
    private static final String URL2 = "http://" + URL1;
    
    public Imdb() {
    }

    @Override
    public boolean accepts(final Movie movie) {
        final String url = movie.getUrl();
        return (null != url) && (url.startsWith(URL1) || url.startsWith(URL2));
    }

    @Override
    public String getDescription(final Movie movie) {
        final String url = movie.getUrl();
        
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(2000).get();
        } catch (IOException ex) {
            Logger.getLogger(Imdb.class.getName()).log(Level.SEVERE, "Cannot read " + url, ex);
            return null;
        }
        
        Element description = doc.select("meta[name=description]").first();
        return description.attr("content");
    }
}
