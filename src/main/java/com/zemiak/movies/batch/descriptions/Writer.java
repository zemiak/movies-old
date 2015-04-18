package com.zemiak.movies.batch.descriptions;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.description.DescriptionReader;
import java.util.List;
import java.util.logging.Level;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

@Named("DescriptionsWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    private final DescriptionReader descriptions = new DescriptionReader();
    @Inject private MovieService service;

    @Override
    public void writeItems(final List list) throws Exception {
        list.stream().filter(obj -> null != obj).forEach(obj -> {
            Movie movie = (Movie) obj;
            String desc = descriptions.read(movie);

            movie.setDescription(desc);
            service.mergeAndSave(movie);

            LOG.log(Level.INFO, "... update description in DB of ", movie.getFileName());
        });
    }
}
