package com.zemiak.movies.batch.newmovies;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import java.util.List;
import java.util.logging.Level;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("NewmoviesWriter")
public class Writer extends AbstractItemWriter {
    @Inject MovieService service;
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Override
    public void writeItems(final List list) throws Exception {
        for (Object obj: list) {
            final String newFile = (String) obj;

            Movie m = service.create(newFile);

            LOG.log(Level.INFO, "Created a new movie ''{0}''/''{1}''...",
                    new Object[]{m.getFileName(), m.getName()});
        }
    }
}
