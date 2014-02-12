package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.CommandLine;
import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.lookup.CustomResourceLookup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 *
 * @author vasko
 */
public class MetadataReader {
    private static final String ERROR = "mp4info: can't open";
    private static final String NAME = "Name: ";
    private static final String GENRE = "Genre: ";
    private static final String COMMENTS = "Comments: ";
    
    private static String mp4info;
    static {
        Properties conf;
        
        conf = new CustomResourceLookup().lookup("com.zemiak.movies");
        mp4info = conf.getProperty("mp4info");
    }
    
    public static MovieMetadata read(final String name, final Movie movie) {
        MovieMetadata metaData = new MovieMetadata(movie);
        List<String> info;
        List<String> params = new ArrayList<>();
        
        params.add(name);
        
        try {
            info = CommandLine.execCmd(mp4info, params);
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            BatchLogger.getLogger(MetadataReader.class.getName()).log(Level.SEVERE, "Can't read metadata for " + name, ex);
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
