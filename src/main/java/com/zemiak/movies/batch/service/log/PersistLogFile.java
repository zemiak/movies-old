package com.zemiak.movies.batch.service.log;

import com.zemiak.movies.domain.CacheClearEvent;
import com.zemiak.movies.service.BatchLogService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PersistLogFile")
public class PersistLogFile implements Batchlet {
    private static final Logger LOG = Logger.getLogger(PersistLogFile.class.getName());

    @Inject
    private JobContext jobCtx;

    @Inject
    private BatchLogService service;

    @Inject
    private Event<CacheClearEvent> events;

    public PersistLogFile() {
    }

    @Override
    public String process() throws Exception {
        final File file = new File(BatchLogger.getLogFileName());
        if (! file.exists()) {
            LOG.log(Level.INFO, "Log file does not exist, not saving...");
            return "does-not-exist";
        }

        persistLogFile();
        events.fire(new CacheClearEvent());

        LOG.log(Level.INFO, "Persisted log file");

        return "saved";
    }

    private void persistLogFile() throws IOException {
        final Path filePath = Paths.get(BatchLogger.getLogFileName());
        final byte[] content = Files.readAllBytes(filePath);
        final String text = new String(content, "UTF-8");

        service.create(text);
    }

    @Override
    public void stop() throws Exception {
    }
}
