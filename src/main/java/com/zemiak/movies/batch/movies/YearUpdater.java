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
public class YearUpdater {
    private static final BatchLogger LOG = BatchLogger.getLogger("YearUpdater");

    @Inject private String path;
    @Inject private MovieService service;

    public void process(final List<String> files) {
        WebMetadataReader reader = new WebMetadataReader(null, null, null, true);

        files.stream()
                .map(fileName -> Paths.get(fileName).toFile().getAbsolutePath())
                .map(fileName -> service.findByFilename(fileName.substring(path.length())))
                .filter(movie -> null != movie)
                .filter(movie -> null == movie.getYear())
                .forEach(movie -> {
                    Integer year = reader.readYear(movie);

                    if (null != year) {
                        movie.setYear(year);
                        service.mergeAndSave(movie);
                        LOG.log(Level.INFO, "... update year in DB of " + movie.getFileName() + " to " + movie.getYear(), movie.getId());
                    }
                });
    }
}