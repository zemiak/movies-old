package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 *
 * @author vasko
 */
public class Csfd implements IDescriptionReader {
    private static final Logger LOG = Logger.getLogger(Csfd.class.getName());
    
    private static final String URL1 = "www.csfd.cz/";
    private static final String URL2 = "http://" + URL1;
    
    public Csfd() {
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
        
        Element description = doc.select("div[data-truncate]").first();
        
        return description.text();
    }
    
    @Override
    public String getReaderName() {
        return "CSFD";
    }

    @Override
    public Map<String, String> getUrlCandidates(final String movieName) {
        Map<String, String> res = new HashMap<>();
        String url;
        
        try {
            url = "http://www.csfd.cz/hledat/?q=" + URLEncoder.encode(movieName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Unsupported UTF-8 encoding.");
            return res;
        }
        
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(2000).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read " + url, ex);
            return res;
        }

        Element list = doc.select("ul[class=ui-image-list").first();
        for (Element li: list.select("li")) {
            final Element desc = li.select("p").first();
            final Element href = li.select("a[class=film").first();
            final String descUrl = href.attr("abs:href");
            res.put(descUrl, href.text() + ": " + desc.text());
        }
        
        return res;
    }
}
