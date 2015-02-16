package com.zemiak.movies.service.thumbnail;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.configuration.Configuration;

public class ThumbnailReader {
    private final IThumbnailReader[] readers;
    private final Configuration conf;

    public ThumbnailReader(final Configuration conf) {
        this.conf = conf;
        this.readers = new IThumbnailReader[]{
            new CsfdThumbnail(),
            new ImdbThumbnail(),
            new GenerateThumbnail(conf.getFfmpeg())
        };
    }

    public void process(final Movie movie) {
        for (IThumbnailReader reader: readers) {
            if (reader.accepts(movie)) {
                reader.setImageFileName(getImageFileName(movie));
                reader.process(movie);
            }
        }
    }

    public String getImageFileName(final Movie movie) {
        final String movieFileName = movie.getFileName();
        final String imageFileName = conf.getImgPath() + movieFileName;
        final int pos = imageFileName.lastIndexOf(".");

        return imageFileName.substring(0, pos) + ".jpg";

    }
}
