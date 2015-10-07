package com.zemiak.movies.service.thumbnail;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.description.Imdb;
import java.io.IOException;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ImdbThumbnail implements IThumbnailReader {
    private final Imdb imdb = new Imdb();
    private static final BatchLogger LOG = BatchLogger.getLogger(ImdbThumbnail.class.getName());
    private String imageFileName;

    public ImdbThumbnail() {
    }

    @Override
    public boolean accepts(final Movie movie) {
        return imdb.accepts(movie);
    }

    @Override
    public String getReaderName() {
        return imdb.getReaderName();
    }

    @Override
    public void setImageFileName(final String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public void process(Movie movie) {
        final String url = movie.getUrl();
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read " + url, ex);
            return;
        }

        Element poster = doc.select("td[id=img_primary]>div[class=image]>a>img").first();
        if (null == poster) {
            LOG.log(Level.SEVERE, "Cannot read poster", null);
            return;
        }

        String imageUrl = poster.attr("src");

        try {
            new CsfdThumbnail().downloadFile(imageUrl, imageFileName);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot fetch poster url {0} file name {1} error {2}",
                    new Object[]{imageUrl, imageFileName, ex});
        }
    }
}
