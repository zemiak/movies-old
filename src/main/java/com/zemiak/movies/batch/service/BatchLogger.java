package com.zemiak.movies.batch.service;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.ConfigurationProvider;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BatchLogger {
    public static BatchLogger getLogger(final String clazz) {
        return new BatchLogger(clazz);
    }

    private final static String LOGFILE = "/tmp/" + BatchLogger.class.getName() + ".log";
    private final Logger logger;
    private final boolean developmentSystem;

    public BatchLogger(final String clazz) {
        this.logger = Logger.getLogger(clazz);
        ConfigurationProvider configuration = new CDILookup().lookup(ConfigurationProvider.class);
        developmentSystem = null == configuration ? true : "true".equals(configuration.getConfigValue("developmentSystem"));
    }

    public void log(final Level level, final String message, final Object[] params) {
        logger.log(level, message, params);

        final LogRecord lr = new LogRecord(level, message + System.getProperty("line.separator"));
        lr.setParameters(params);
        lr.setLoggerName(logger.getName());

        logMessageToFile(new SimpleFormatter().format(lr));
    }

    public void log(final Level level, final String message, final Object param) {
        if (developmentSystem || (!developmentSystem && level.intValue() >= Level.INFO.intValue())) {
            // on PROD system, log only >= INFO messages
            log(level, message, new Object[]{param});
        }
    }

    public void info(final String message) {
        log(Level.INFO, message, null);
    }

    private void logMessageToFile(final String message) {
        OutputStream file;

        try {
            file = new FileOutputStream(LOGFILE, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BatchLogger.class.getName()).log(Level.SEVERE, "Cannot open LOG file", ex);
            return;
        }

        try {
            file.write(message.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(BatchLogger.class.getName()).log(Level.SEVERE, "Cannot write to LOG file", ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(BatchLogger.class.getName()).log(Level.SEVERE, "Cannot close LOG file", ex);
            }
        }
    }

    public static void deleteLogFile() {
        final File file = new File(LOGFILE);
        file.delete();
    }

    public static String getLogFileName() {
        return LOGFILE;
    }
}
