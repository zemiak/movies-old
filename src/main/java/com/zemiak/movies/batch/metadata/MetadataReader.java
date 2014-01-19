package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.CommandLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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
        Context context;
        Properties conf;
        
        try {
            context = new InitialContext();
            conf = (Properties) context.lookup("com.zemiak.movies");
            mp4info = conf.getProperty("mp4info");
        } catch (NamingException ex) {
            Logger.getLogger(MetadataReader.class.getName()).log(Level.SEVERE, "Cannot initialize context", ex);
            mp4info = null;
        }
    }
    
    public static MovieMetadata read(final String name) {
        MovieMetadata metaData = new MovieMetadata();
        List<String> info;
        List<String> params = new ArrayList<>();
        
        params.add(name);
        
        try {
            CommandLine.isDebug = false;
            info = CommandLine.execCmd(mp4info, params);
        } catch (IOException | InterruptedException | IllegalStateException ex) {
            CommandLine.isDebug = true;
            Logger.getLogger(MetadataReader.class.getName()).log(Level.SEVERE, "Can't read metadata for " + name, ex);
            return null;
        }
        
        CommandLine.isDebug = true;

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
