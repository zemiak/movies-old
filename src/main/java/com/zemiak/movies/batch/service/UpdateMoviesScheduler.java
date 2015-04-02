package com.zemiak.movies.batch.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UpdateMoviesScheduler {
    private static final Logger LOG = Logger.getLogger(UpdateMoviesScheduler.class.getName());

    @Inject
    private BatchRunner runner;

    @Schedule(dayOfWeek="*", hour="03", minute="10")
    public void start() {
        if (runner.isUpdateMoviesRunning()) {
            LOG.log(Level.SEVERE, "Update Job is already running !!!");
        } else {
            LOG.log(Level.INFO, "Update Movies started");
            runner.runUpdateCollection();
        }
    }
}
