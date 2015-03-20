package com.zemiak.movies.batch.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Stateless;

/**
 *
 * @author vasko
 */
@Stateless
public class BatchRunner {
    private static final Logger LOG = Logger.getLogger(BatchRunner.class.getName());

    JobOperator operator;
    Long updateMoviesJob = null;

    public BatchRunner() {
    }

    @PostConstruct
    public void init() {
        operator = BatchRuntime.getJobOperator();
    }

    public void runUpdateCollection() {
        if (isRunning(updateMoviesJob)) {
            LOG.log(Level.INFO, "Job update-movies is still running");
            return;
        }

        updateMoviesJob = operator.start("update-movies", null);
        LOG.log(Level.INFO, "Submitted update-movies {0}", updateMoviesJob);
    }

    private boolean isRunning(Long id) {
        if (null == id) {
            return false;
        }

        BatchStatus status = operator.getJobExecution(id).getBatchStatus();
        return (status == BatchStatus.STARTING || status == BatchStatus.STOPPING
                || status == BatchStatus.STARTED);
    }

    public boolean isUpdateMoviesRunning() {
        return isRunning(updateMoviesJob);
    }

    public Long getUpdateMoviesJob() {
        return updateMoviesJob;
    }
}
