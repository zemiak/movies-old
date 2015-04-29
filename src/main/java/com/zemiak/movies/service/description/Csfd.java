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
import org.jsoup.select.Elements;

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
            LOG.log(Level.SEVERE, "Cannot read {0}: {1}", new Object[]{url, ex});
            return null;
        }

        Elements description = doc.select("div[data-truncate]");

        return null != description ? description.text() : "";
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
            LOG.log(Level.SEVERE, "Unsupported UTF-8 encoding.", ex);
            return res;
        }

        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read " + url, ex);
            return res;
        }

        Elements list = doc
                .select("div[id=search-films] > div[class=content] > ul[class=ui-image-list js-odd-even]");
        if (null == list) {
            return res;
        }

        Elements elements = list.select("li");

        elements.stream().forEach(li -> {
            final Elements href = li.select("div > h3 > a");
            if (null == href) {
                return;
            }

            final String desc = href.first().text();
            final String descUrl = href.first().absUrl("href");

            res.add(new UrlDTO(descUrl, getReaderName(), href.text(), desc));
        });

        return res;
    }
}
