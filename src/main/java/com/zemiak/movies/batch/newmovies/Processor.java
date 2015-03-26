package com.zemiak.movies.batch.newmovies;

import com.zemiak.movies.batch.service.RemoveFileList;
import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.service.MovieService;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

@Named("NewmoviesProcessor")
public class Processor implements ItemProcessor {
    private static final BatchLogger LOG = BatchLogger.getLogger(Processor.class.getName());

    @Inject private MovieService service;
    @Inject private String path;

    private String prefix;

    @PostConstruct
    public void init() {
        prefix = path;
    }

    @Override
    public Object processItem(final Object movie) throws Exception {
        final String fileName = getRelativeFilename((String) movie);
        if (null == service.findByFilename(fileName)) {
            LOG.log(Level.INFO, "Found a new file: {0}", fileName);
            return fileName;
        }

        return null;
    }

    private String getRelativeFilename(final String absoluteFilename) {
        String absoluteWithSlashes = absoluteFilename.replaceAll(RemoveFileList.PATH_SEPARATOR, "/");
        if (absoluteWithSlashes.startsWith(prefix)) {
            absoluteWithSlashes = absoluteWithSlashes.substring(prefix.length());
        }

        return absoluteWithSlashes;
    }
}
