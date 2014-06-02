package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.CommandLine;
import com.zemiak.movies.description.DescriptionReader;
import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.configuration.Configuration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("MetadataWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Inject Configuration conf;
    private final DescriptionReader descriptions = new DescriptionReader();
    @Inject MovieService service;

    private static final String GENRE = "-g";
    private static final String NAME = "-s";
    private static final String COMMENTS = "-c";

    @Override
    public void writeItems(final List list) throws Exception {
        for (Object obj : list) {
            MovieMetadata data = (MovieMetadata) obj;

            if (null != data) {
                String fileName = conf.getPath() + data.getMovie().getFileName();
                updateName(fileName, data);
                updateGenre(fileName, data);
                updateComment(fileName, data);
            }
        }
    }

    private void update(final String fileName, final String commandLineSwitch, final String value) {
        final List<String> params = new ArrayList<>();

        params.add(commandLineSwitch);
        params.add(value);
        params.add(fileName);

        try {
            CommandLine.execCmd(conf.getMp4tags(), params);
            LOG.info(String.format("Updated %s with %s on %s", commandLineSwitch, value, fileName));
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, "Cannot update " + commandLineSwitch + " for " + fileName, ex);
        }
    }

    private void updateName(final String fileName, final MovieMetadata data) {
        if (! data.isNameEqual()) {
            update(fileName, NAME, data.getMovie().getName());
        }
    }

    private void updateGenre(final String fileName, final MovieMetadata data) {
        if (! data.isGenreEqual()) {
            update(fileName, GENRE, data.getMovie().composeGenreName());
        }
    }

    private void updateComment(final String fileName, final MovieMetadata data) {
        if (data.commentsShouldBeUpdatedQuiet()) {
            final String desc = descriptions.read(data.getMovie());

            if (null != desc && !desc.trim().isEmpty() && !desc.equals(data.getComments())) {
                update(fileName, COMMENTS, desc);

                data.getMovie().setDescription(desc);
                service.mergeAndSave(data.getMovie());
            }
        }
    }
}
