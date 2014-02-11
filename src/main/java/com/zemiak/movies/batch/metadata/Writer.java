package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.CommandLine;
import com.zemiak.movies.batch.metadata.description.DescriptionReader;
import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Named("MetadataWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Resource(name = "com.zemiak.movies") private Properties conf;
    @Inject private DescriptionReader descriptions;
    @PersistenceContext private EntityManager em;

    private static final String GENRE = "-g";
    private static final String NAME = "-s";
    private static final String COMMENTS = "-c";

    @Override
    public void writeItems(final List list) throws Exception {
        for (Object obj : list) {
            Movie movie = (Movie) obj;

            if (null != movie) {
                String fileName = conf.getProperty("path") + movie.getFileName();
                MovieMetadata data = MetadataReader.read(fileName);

                updateName(fileName, movie, data);
                updateGenre(fileName, movie, data);
                updateComment(fileName, movie, data);
            }
        }
    }

    private void update(final String fileName, final String commandLineSwitch, final String value) {
        final List<String> params = new ArrayList<>();

        params.add(commandLineSwitch);
        params.add(value);
        params.add(fileName);

        try {
            CommandLine.execCmd(conf.getProperty("mp4tags"), params);
            LOG.info(String.format("Updating %s with %s on %s", commandLineSwitch, value, fileName));
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, "Cannot update " + commandLineSwitch + " for " + fileName, ex);
        }
    }

    private void updateName(final String fileName, final Movie movie, final MovieMetadata data) {
        if (null == data.getName() || !data.getName().equals(movie.getName())) {
            update(fileName, NAME, movie.getName());
        }
    }

    private void updateGenre(final String fileName, final Movie movie, final MovieMetadata data) {
        if (null == data.getGenre()|| !data.getGenre().equals(movie.composeGenreName())) {
            update(fileName, GENRE, movie.composeGenreName());
        }
    }

    private void updateComment(final String fileName, final Movie movie, final MovieMetadata data) {
        if (data.commentsShouldBeUpdated(movie)) {
            final String desc = descriptions.read(movie);

            if (null != desc && !desc.trim().isEmpty()) {
                update(fileName, COMMENTS, desc);
                
                movie.setDescription(desc);
                em.merge(movie);
                em.flush();
            }
        }
    }
}
