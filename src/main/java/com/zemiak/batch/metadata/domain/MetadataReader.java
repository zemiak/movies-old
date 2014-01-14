package com.zemiak.batch.metadata.domain;

import com.zemiak.batch.metadata.CommandLine;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vasko
 */
public class MetadataReader {
    private static final String MP4INFO = "mp4info";
    private static final String ERROR = "mp4info: can't open";
    private static final String NAME = " Name: ";
    private static final String GENRE = " Genre: ";
    private static final String COMMENTS = " Comments: ";
    
    public static MovieMetadata read(final String name) {
        MovieMetadata metaData = new MovieMetadata();
        List<String> info;
        
        try {
            info = CommandLine.execCmd(MP4INFO + " \"" + name + "\"");
        } catch (IOException ex) {
            Logger.getLogger(MetadataReader.class.getName()).log(Level.SEVERE, "Can't read metadata for " + name, ex);
            return null;
        }

        for (String line: info) {
            line = line.trim();
            
            if (line.startsWith(ERROR)) {
                return null;
            }
            
            if (line.startsWith(NAME)) {
                metaData.setName(line.substring(NAME.length()));
            }
            
            if (line.startsWith(GENRE)) {
                metaData.setGenre(line.substring(GENRE.length()));
            }
            
            if (line.startsWith(COMMENTS)) {
                metaData.setComments(line.substring(COMMENTS.length()));
            }
        }
        
        return metaData;
    }
}
