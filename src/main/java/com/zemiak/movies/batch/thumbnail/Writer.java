package com.zemiak.movies.batch.thumbnail;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.thumbnail.ThumbnailReader;
import java.util.List;
import java.util.logging.Level;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("ThumbnailsWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger("ThumbnailsWriter");

    @Inject private String path;
    @Inject private String imgPath;
    @Inject private String ffmpeg;
    @Inject private String developmentSystem;

    @Override
    public void writeItems(List list) throws Exception {
        ((List<Movie>) list)
                .stream()
                .forEach(movie -> {
                    if (new ThumbnailReader(imgPath, path, ffmpeg, "true".equals(developmentSystem)).process(movie)) {
                        LOG.info("Generated a thumbnail " + movie.getPictureFileName());
                    } else {
                        LOG.log(Level.SEVERE, "Error generating a thumbnail {0}", movie.getPictureFileName());
                    }
                });
    }
}
