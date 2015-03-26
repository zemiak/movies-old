package com.zemiak.movies.batch.service.log;

import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PrepareLogFile")
public class PrepareLogFile implements Batchlet {
    @Inject
    private JobContext jobCtx;

    public PrepareLogFile() {
    }

    @Override
    public String process() throws Exception {
        BatchLogger.deleteLogFile();

        return "done";
    }

    @Override
    public void stop() throws Exception {
    }
}
