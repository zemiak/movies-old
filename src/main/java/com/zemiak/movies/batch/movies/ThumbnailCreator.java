package com.zemiak.movies.batch.movies;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.scraper.WebMetadataReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ThumbnailCreator {
    private static final BatchLogger LOG = BatchLogger.getLogger("ThumbnailCreator");

    @Inject private String path;
    @Inject private String imgPath;
    @Inject private String ffmpeg;
    @Inject Boolean developmentSystem;

    @Inject private MovieService service;

    public void process(final List<String> files) {
        files.stream()
                .map(fileName -> Paths.get(fileName).toFile().getAbsolutePath())
                .map(fileName -> service.findByFilename(fileName.substring(path.length())))
                .filter(movie -> null != movie)
                .filter(movie -> !Paths.get(imgPath, "movie", movie.getPictureFileName()).toFile().exists())
                .filter(movie -> null != movie.getWebPage())
                .forEach(movie -> {
                    WebMetadataReader reader = new WebMetadataReader(imgPath, path, ffmpeg, developmentSystem);

                    if (reader.processThumbnail(movie)) {
                        LOG.log(Level.FINE, "Generated a thumbnail {0}", movie.getPictureFileName());
                    } else {
                        LOG.log(Level.SEVERE, "Error generating a thumbnail {0}", movie.getPictureFileName());
                    }
                });
    }
}
