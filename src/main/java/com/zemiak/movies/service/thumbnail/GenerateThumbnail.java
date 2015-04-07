package com.zemiak.movies.service.thumbnail;

import com.zemiak.movies.batch.service.CommandLine;
import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.strings.Joiner;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class GenerateThumbnail implements IThumbnailReader {
    private static final BatchLogger LOG = BatchLogger.getLogger(GenerateThumbnail.class.getName());
    private String imageFileName;
    private final String ffmpegPath;
    private final boolean developmentSystem;

    public GenerateThumbnail(final String ffmpegPath, final boolean developmentSystem) {
        this.ffmpegPath = ffmpegPath;
        this.developmentSystem = developmentSystem;
    }

    @Override
    public boolean accepts(Movie movie) {
        return true;
    }

    @Override
    public String getReaderName() {
        return "General";
    }

    @Override
    public void setImageFileName(final String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public void process(final Movie movie) {
        final String movieFileName = movie.getFileName();

        final List<String> params = Arrays.asList(
            "-s", "220", "-i", movieFileName, "-o", imageFileName
        );

        try {
            if (!developmentSystem) {
                CommandLine.execCmd(ffmpegPath, params);
            } else {
                LOG.log(Level.INFO, "dry run:{0} {1}", new Object[]{ffmpegPath, null == params ? "" : Joiner.join(params, "|")});
            }

            LOG.log(Level.INFO, "Generated thumbnail {0} ...", imageFileName);
        } catch (IllegalStateException | InterruptedException | IOException ex) {
            LOG.log(Level.SEVERE, "DID NOT generate thumbnail {0}: {1} ...",
                    new Object[]{imageFileName, ex.getMessage()});
        }
    }
}
