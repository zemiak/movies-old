package com.zemiak.movies.batch.service.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author vasko
 */
public class BatchLogger {
    public static BatchLogger getLogger(final String clazz) {
        return new BatchLogger(clazz);
    }

    private final static String LOGFILE = "/tmp/batch.log";
    private final Logger logger;

    public BatchLogger(final String clazz) {
        this.logger = Logger.getLogger(clazz);
    }

    public void log(final Level level, final String message, final Object[] params) {
        logger.log(level, message, params);

        final LogRecord lr = new LogRecord(level, message + System.getProperty("line.separator"));
        lr.setParameters(params);
        lr.setLoggerName(logger.getName());

        logMessageToFile(new SimpleFormatter().format(lr));
    }

    public void log(final Level level, final String message, final Object param) {
        log(level, message, new Object[]{param});
    }

    public void info(final String message) {
        log(Level.INFO, message, null);
    }

    private void logMessageToFile(final String message) {
        OutputStream file;

        try {
            file = new FileOutputStream(LOGFILE);
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
