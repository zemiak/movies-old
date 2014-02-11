package com.zemiak.movies.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author vasko
 */
@Stateless
public class UpdateMovies {
    private static final Logger LOG = Logger.getLogger(UpdateMovies.class.getName());
    
    @Inject
    BatchRunner runner;
    
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
