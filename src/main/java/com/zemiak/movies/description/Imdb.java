package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
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
    private static final Logger LOG = Logger.getLogger(Imdb.class.getName());
    
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
        return getDescription(movie.getUrl());
        
    }
    
    private String getDescription(final String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(2000).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read " + url, ex);
            return null;
        }
        
        Element description = doc.select("meta[name=description]").first();
        return description.attr("content");
    }
    
    @Override
    public String getReaderName() {
        return "IMDB";
    }

    @Override
    public Map<String, String> getUrlCandidates(final String movieName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
