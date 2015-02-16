package com.zemiak.movies.batch.thumbnail;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.configuration.Configuration;
import com.zemiak.movies.service.thumbnail.ThumbnailReader;
import java.io.File;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("ThumbnailsWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Inject
    private Configuration conf;

    @Override
    public void writeItems(List list) throws Exception {
        ((List<Movie>) list)
                .stream()
                .filter(movie -> !thumbnailExists(movie))
                .forEach(movie -> {
                    new ThumbnailReader(conf).process(movie);
                });
    }

    private boolean thumbnailExists(final Movie movie) {
        return new File(new ThumbnailReader(conf).getImageFileName(movie)).isFile();
    }
}
