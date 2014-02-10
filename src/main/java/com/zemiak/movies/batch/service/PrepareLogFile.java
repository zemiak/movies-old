package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.service.log.LoggerInstance;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("PrepareLogFile")
public class PrepareLogFile implements Batchlet {
    @Inject
    JobContext jobCtx;
    
    public PrepareLogFile() {
    }

    @Override
    public String process() throws Exception {
        LoggerInstance.deleteLogFile();
        
        return "done";
    }

    @Override
    public void stop() throws Exception {
    }
}
