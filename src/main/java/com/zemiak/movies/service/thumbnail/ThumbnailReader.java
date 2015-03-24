package com.zemiak.movies.service.thumbnail;

import com.zemiak.movies.domain.Movie;

public class ThumbnailReader {
    private final IThumbnailReader[] readers;
    private final String imgPath;

    public ThumbnailReader(String imgPath, String ffmpeg) {
        this.readers = new IThumbnailReader[]{
            new CsfdThumbnail(),
            new ImdbThumbnail(),
            new GenerateThumbnail(ffmpeg)
        };
        
        this.imgPath = imgPath;
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
        final String imageFileName = imgPath + movieFileName;
        final int pos = imageFileName.lastIndexOf(".");

        return imageFileName.substring(0, pos) + ".jpg";

    }
}
