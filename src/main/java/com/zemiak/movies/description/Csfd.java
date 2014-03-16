package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
    private static final String SEARCH_URL = URL2 + "hledat/?q=";
    
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
            doc = Jsoup.connect(url).timeout(5000).get();
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

        final Element list = doc
                .select("div[id=search-films]").first()
                .select("div[class=content]").first()
                .select("ul[class=ui-image-list js-odd-even]").first();
        for (Element li: list.select("li")) {
            final Element desc = li.select("p").first();
            final Element href = li.select("h3").first().select("a").first();
            final String descUrl = href.absUrl("href");
            
            res.add(new UrlDTO(descUrl, getReaderName(), href.text(), desc.text()));
        }
        
        return res;
    }
}
