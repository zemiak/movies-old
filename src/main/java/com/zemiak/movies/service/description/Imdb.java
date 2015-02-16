package com.zemiak.movies.service.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
    private static final String SEARCH_URL = URL2 + "find?q=";
    
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
            doc = Jsoup.connect(url).timeout(5000).get();
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
    public List<UrlDTO> getUrlCandidates(final String movieName) {
        List<UrlDTO> res = new ArrayList<>();
        String url;
        
        try {
            url = SEARCH_URL + URLEncoder.encode(movieName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Unsupported UTF-8 encoding.");
            return res;
        }
        
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read " + url, ex);
            return res;
        }
        
        for (Element result: doc.select("td[class=result_text]")) {
            final Element href = result.select("a").first();
            res.add(new UrlDTO(href.absUrl("href"), getReaderName(), href.text(), result.text()));
        }
        
        return res;
    }
}
