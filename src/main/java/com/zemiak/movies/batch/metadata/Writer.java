package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.CommandLine;
import com.zemiak.movies.batch.metadata.description.DescriptionReader;
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

/**
 *
 * @author vasko
 */
@Named("MetadataWriter")
public class Writer extends AbstractItemWriter {
    private static final Logger LOG = Logger.getLogger(Writer.class.getName());
    
    @Resource(name = "com.zemiak.movies") private Properties conf;
    @Inject private DescriptionReader descriptions;
            
    private static final String GENRE = "-g";
    private static final String NAME = "-s";
    private static final String COMMENTS = "-c";

    @Override
    public void writeItems(List list) throws Exception {
        for (Object obj : list) {
            Movie movie = (Movie) obj;
            
            if (null != movie) {
                String fileName = conf.getProperty("path") + movie.getFileName();
                
                updateName(fileName, movie);
                updateGenre(fileName, movie);
                updateComment(fileName, movie);
                
                LOG.log(Level.INFO, "MetadataWriter: Updated movie metadata: {0} ...", 
                        fileName);
            } else {
                LOG.log(Level.SEVERE, "MetadataWriter: NOT Updated movie metadata: #{0} ...", 
                        movie.getFileName());
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
        } catch (IOException | InterruptedException | IllegalStateException ex) {
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
