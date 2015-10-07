package com.zemiak.movies.batch.service;

import com.zemiak.movies.service.BatchLogService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PersistLogFile {
    private static final Logger LOG = Logger.getLogger(PersistLogFile.class.getName());

    @Inject
    private BatchLogService service;

    public void persist() {
        final File file = new File(BatchLogger.getLogFileName());
        if (! file.exists()) {
            LOG.log(Level.INFO, "Log file does not exist, not saving...");
            return;
        }

        try {
            persistLogFile();
            LOG.log(Level.INFO, "Persisted log file");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error persisting log file", ex);
        }
    }

    private void persistLogFile() throws IOException {
        final Path filePath = Paths.get(BatchLogger.getLogFileName());
        final byte[] content = Files.readAllBytes(filePath);
        final String text = new String(content, "UTF-8");

        service.create(text);
    }
}
