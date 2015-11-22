package com.zemiak.movies.service.scraper;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MovieDocumentCache {
    private static final BatchLogger LOG = BatchLogger.getLogger(MovieDocumentCache.class.getName());

    private Document movieDoc;
    private Movie movie;

    public Document getMovieDocument(final Movie requestedMovie) {
        if (null == movie || !movie.getId().equals(requestedMovie.getId())) {
            movie = requestedMovie;
            movieDoc = getDocument(movie.getUrl());
        }

        return movieDoc;
    }

    private Document getDocument(final String url) {
        try {
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read {0}: {1}", new Object[]{url, ex});
            return null;
        }
    }
}
