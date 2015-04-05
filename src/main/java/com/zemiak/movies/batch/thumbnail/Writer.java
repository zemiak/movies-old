package com.zemiak.movies.batch.thumbnail;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.thumbnail.ThumbnailReader;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("ThumbnailsWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Inject
    private String imgPath;

    @Inject
    private String ffmpeg;

    @Override
    public void writeItems(List list) throws Exception {
        ((List<Movie>) list)
                .stream()
                .forEach(movie -> {
                    new ThumbnailReader(imgPath, ffmpeg).process(movie);
                });
    }
}
