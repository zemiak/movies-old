package com.zemiak.movies.batch.newmovies;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import java.util.List;
import java.util.logging.Level;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("NewmoviesWriter")
public class Writer extends AbstractItemWriter {
    @Inject private MovieService service;
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Override
    public void writeItems(final List list) throws Exception {
        list.stream().map(obj -> service.create((String) obj)).forEach(obj -> {
            Movie m = (Movie) obj;
            LOG.log(Level.INFO, "Created a new movie ''{0}''/''{1}'', id {2}...",
                    new Object[]{m.getFileName(), m.getName(), m.getId()});
        });
    }
}
