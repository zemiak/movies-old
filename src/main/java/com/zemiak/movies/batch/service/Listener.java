package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.batch.service.log.LoggerInstance;
import java.util.logging.Level;
import javax.batch.api.listener.JobListener;
import javax.batch.api.listener.StepListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

/**
 *
 * @author vasko
 */
public class Listener implements JobListener, StepListener {
    private static final LoggerInstance LOG = BatchLogger.getLogger(Listener.class.getName());
    
    @Inject
    JobContext jobCtx;
    
    @Inject
    StepContext stepCtx;

    @Override
    public void beforeJob() throws Exception {
        LOG.log(Level.INFO, "Job starting {0}", jobCtx.getJobName());
    }

    @Override
    public void afterJob() {
        LOG.log(Level.INFO, "Job finished {0}", jobCtx.getJobName());
    }

    @Override
    public void beforeStep() throws Exception {
        LOG.log(Level.INFO, "Start step {0}", stepCtx.getStepName());
    }

    @Override
    public void afterStep() throws Exception {
        LOG.log(Level.INFO, "End step {0}", stepCtx.getStepName());
    }
}
