package com.zemiak.movies.batch.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vasko
 */
public class CommandLine {
    private static final Logger LOG = Logger.getLogger(CommandLine.class.getName());
    public static boolean isDebug = true;
    
    public static List<String> execCmd(final String cmd) throws IOException {
        List<String> lines;
        
        if (isDebug) {
            LOG.log(Level.INFO, "execCmd: ''{0}''", cmd);
            return new ArrayList<>();
        }
        
        try (InputStream stream = Runtime.getRuntime().exec(cmd).getInputStream(); 
                Scanner scanner = new Scanner(stream)) {
            lines = new ArrayList<>();
            
            while (scanner.hasNext()) {
                lines.add(scanner.next());
            }
        }
        
        return lines;
    }
}
