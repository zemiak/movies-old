package com.zemiak.movies.batch.movies;

import com.zemiak.movies.batch.service.BatchLogger;
import com.zemiak.movies.batch.service.CommandLine;
import com.zemiak.movies.batch.service.RefreshStatistics;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import com.zemiak.movies.service.description.DescriptionReader;
import com.zemiak.movies.strings.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class MetadataRefresher {
    private static final BatchLogger LOG = BatchLogger.getLogger(MetadataRefresher.class.getName());

    private final DescriptionReader descriptions = new DescriptionReader();
    @Inject private MovieService service;
    @Inject private String mp4tags;
    @Inject private String path;
    @Inject Boolean developmentSystem;
    @Inject RefreshStatistics stats;

    private static final String GENRE = "-g";
    private static final String NAME = "-s";
    private static final String COMMENTS = "-c";

    private void update(final String fileName, final String commandLineSwitch, final String value) {
        final List<String> params = new ArrayList<>();

        params.add(commandLineSwitch);
        params.add(value);
        params.add(fileName);

        try {
            if (!developmentSystem) {
                CommandLine.execCmd(mp4tags, params);
            } else {
                LOG.log(Level.INFO, "dry run:{0} {1}", new Object[]{mp4tags, null == params ? "" : Joiner.join(params, "|")});
            }

            LOG.info(String.format("Updated %s with %s on %s", commandLineSwitch, value, fileName));
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            Logger.getLogger(MetadataRefresher.class.getName()).log(Level.SEVERE, "Cannot update " + commandLineSwitch + " for " + fileName, ex);
        }
    }

    private void updateName(final String fileName, final MovieMetadata data) {
        if (! data.isNameEqual()) {
            update(fileName, NAME, data.getMovieName());
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

    public void process(final List<String> files) {
        files.stream().forEach(fileName -> {
            Movie movie = service.findByFilename(fileName.substring(path.length()));
            MovieMetadata data = new MetadataReader(fileName, movie, service).get();


            if (null != movie && null != data) {
                if (! data.isMetadataEqual()) {
                    LOG.info("Metadata: going to update " + fileName);
                    updateName(fileName, data);
                    updateGenre(fileName, data);
                    updateComment(fileName, data);
                    stats.incrementUpdated();
                }
            }
        });
    }
}
