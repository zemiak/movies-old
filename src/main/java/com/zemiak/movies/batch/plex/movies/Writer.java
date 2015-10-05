package com.zemiak.movies.batch.plex.movies;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PlexMoviesWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Inject private JobContext jobCtx;
    @Inject private MovieWriter movieWriter;
    @Inject private SerieItemWriter serieItemWriter;

    @Override
    public void writeItems(final List list) throws Exception {
        String plexFolder = jobCtx.getProperties().getProperty("plexFolder");
        list.stream().filter(obj -> null != obj).forEach(obj -> {
            Movie movie = (Movie) obj;
            Serie serie = movie.getSerie();

            if (null != serie && serie.getTvShow()) {
                try {
                    serieItemWriter.process(plexFolder, movie);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "IO/Error creating a link for serie item " + movie.getFileName(), ex);
                }
            } else {
                try {
                    movieWriter.process(plexFolder, movie);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, "IO/Error creating a link for movie " + movie.getFileName(), ex);
                }
            }
        });
    }
}
