package com.zemiak.movies.service.thumbnail;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.description.Csfd;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CsfdThumbnail implements IThumbnailReader {
    private final Csfd csfd = new Csfd();
    private static final BatchLogger LOG = BatchLogger.getLogger(CsfdThumbnail.class.getName());
    private String imageFileName;

    public CsfdThumbnail() {
    }

    @Override
    public boolean accepts(final Movie movie) {
        return csfd.accepts(movie);
    }

    @Override
    public String getReaderName() {
        return csfd.getReaderName();
    }

    @Override
    public void setImageFileName(final String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public void process(final Movie movie) {
        final String url = movie.getUrl();
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot read " + url, ex);
            return;
        }

        Element poster = doc.select("img[class=film-poster]").first();
        if (null == poster) {
            LOG.log(Level.SEVERE, "Cannot read poster", null);
            return;
        }

        String imageUrl = poster.attr("src");
        if (! imageUrl.startsWith("http")) {
            imageUrl = "http:" + imageUrl;
        }

        try {
            downloadFile(imageUrl, imageFileName);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot fetch poster url {0} file name {1} error {2}",
                    new Object[]{imageUrl, imageFileName, ex});
        }
    }

    public void downloadFile(final String imageUrl, final String fileLocation) throws IOException {
        Response resultImageResponse = Jsoup.connect(imageUrl).ignoreContentType(true).execute();

        try (FileOutputStream out = new FileOutputStream(new java.io.File(fileLocation))) {
            out.write(resultImageResponse.bodyAsBytes());
        }
    }
}
