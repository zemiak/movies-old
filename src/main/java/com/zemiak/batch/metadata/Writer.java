package com.zemiak.batch.metadata;

import com.zemiak.batch.metadata.description.DescriptionReader;
import com.zemiak.movies.domain.Movie;
import java.io.IOException;
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
    private static final Logger LOG = Logger.getLogger(Writer.class.getName());
    
    private static final String MP4TAGS = "mp4tags";
    private static final String GENRE = "-g";
    private static final String NAME = "-s";
    private static final String COMMENTS = "-c";
    
    @Inject private Processor processor;
    @Inject private DescriptionReader descriptions;

    @Override
    public void writeItems(List list) throws Exception {
        for (Object obj : list) {
            String fileName = (String) obj;
            Movie movie = processor.find(fileName);
            
            if (null != movie) {
                updateName(fileName, movie);
                updateGenre(fileName, movie);
                updateComment(fileName, movie);
            }
        }
    }
    
    private void update(final String fileName, final String commandLineSwitch, final String value) {
        try {
            CommandLine.execCmd(MP4TAGS + " " + commandLineSwitch + " \"" + value + "\" \"" + fileName + "\"");
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, "Cannot update " + commandLineSwitch + " for " + fileName, ex);
        }
    }

    private void updateName(String fileName, Movie movie) {
        update(fileName, NAME, movie.getName());
    }

    private void updateGenre(String fileName, Movie movie) {
        update(fileName, GENRE, movie.composeGenreName());
    }

    private void updateComment(String fileName, Movie movie) {
        if (null == movie.getDescription() || movie.getDescription().trim().isEmpty() || "''".equals(movie.getDescription())) {
            final String desc = descriptions.read(movie.getUrl());
            
            if (null != desc && !desc.trim().isEmpty()) {
                update(fileName, COMMENTS, desc);
            }
        }
    }
}
