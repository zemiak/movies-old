package com.zemiak.movies.service.thumbnail;

import com.zemiak.movies.domain.Movie;
import java.nio.file.Paths;

public class ThumbnailReader {
    private final IThumbnailReader[] readers;
    private final String imgPath;

    public ThumbnailReader(String imgPath, String ffmpeg, boolean developmentSystem) {
        this.readers = new IThumbnailReader[]{
            new CsfdThumbnail(),
            new ImdbThumbnail(),
            new GenerateThumbnail(ffmpeg, developmentSystem)
        };

        this.imgPath = imgPath;
    }

    public boolean process(final Movie movie) {
        for (IThumbnailReader reader: readers) {
            if (reader.accepts(movie)) {
                reader.setImageFileName(getImageFileName(movie));
                reader.process(movie);

                return true;
            }
        }

        return false;
    }

    public String getImageFileName(final Movie movie) {
        return Paths.get(imgPath, "movie", movie.getPictureFileName()).toString();
    }
}
