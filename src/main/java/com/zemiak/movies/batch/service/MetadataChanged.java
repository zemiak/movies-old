package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.service.log.BatchLogger;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.Batchlet;
import javax.inject.Inject;
import javax.inject.Named;

@Named("MetadataChanged")
public class MetadataChanged implements Batchlet {
    private static final Logger LOG = Logger.getLogger(MetadataChanged.class.getName());
    private static final String INDICATOR_FILE_NAME = "itunes-refresh";

    @Inject private String path;

    public MetadataChanged() {
    }

    @Override
    public String process() throws Exception {
        final File file = new File(BatchLogger.getLogFileName());
        if (! file.exists()) {
            LOG.log(Level.INFO, "Log file does not exist, not creating the indicator file...");
            return "does-not-exist";
        }

        return createIndicatorFile() ? "saved" : "error";
    }

    @Override
    public void stop() throws Exception {
        // does nothing, must be implemented
    }

    private boolean createIndicatorFile() {
        final File mainDir = new File(path);
        final File file = new File(mainDir.getAbsolutePath() + RemoveFileList.PATH_SEPARATOR + INDICATOR_FILE_NAME);

        try {
            if (file.createNewFile()) {
                LOG.log(Level.INFO, "Created the indicator file {0}", file.getAbsolutePath());
                return true;
            } else {
                LOG.log(Level.SEVERE, "Cannot create an indicator file {0}", file.getAbsolutePath());
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot create an indicator file {0}: {1}", new Object[]{file.getAbsolutePath(), ex});
        }

        return false;
    }
}
