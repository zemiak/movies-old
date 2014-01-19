package com.zemiak.movies.batch.service;

import com.google.gwt.thirdparty.guava.common.base.Joiner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vasko
 */
public class CommandLine {
    private static final Logger LOG = Logger.getLogger(CommandLine.class.getName());
    public static boolean isDebug = true;
    
    public static List<String> execCmd(final String cmd, final List<String> arguments) 
            throws IOException, InterruptedException, IllegalStateException {
        List<String> lines = new ArrayList<>();
        final List<String> command = new ArrayList<>(arguments);
        
        LOG.log(Level.INFO, "execCmd: ''{0}''", cmd);
        
        if (isDebug) {
            return lines;
        }
        
        command.add(0, cmd);
        Process process = Runtime.getRuntime().exec(command.toArray(new String[]{}));
        
        int exitValue = process.waitFor();
        if (exitValue != 0) {
            LOG.log(Level.INFO, "... execCmd: error code is {0}", exitValue);
            throw new IllegalStateException("Exit code " + exitValue + " instead of success");
        }
        
        try (InputStream stream = process.getInputStream();) {
            for (String line: streamToString(stream).split(System.getProperty("line.separator"))) {
                lines.add(line);
            }
        }
        
        LOG.log(Level.INFO, "... execCmd: output is {0}", Joiner.on("|").join(lines));
        
        return lines;
    }
    
    private static String streamToString(final InputStream stream) throws IOException {
        char[] buff = new char[1024];
        Writer stringWriter = new StringWriter();

        try {
            Reader bReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            int n;
            while ((n = bReader.read(buff)) != -1) {
                stringWriter.write(buff, 0, n);
            }
        } finally {
            stringWriter.close();
        }

        return stringWriter.toString();
    }

}