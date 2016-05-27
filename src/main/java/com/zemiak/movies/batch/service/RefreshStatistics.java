package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.service.logs.BatchLogger;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RefreshStatistics {
    private BatchLogger LOG = null;

    private int created;
    private int updated;

    @Inject Boolean developmentSystem;

    @PostConstruct
    public void reset() {
        created = 0;
        updated = 0;
    }

    public void dump() {
        dump(developmentSystem || haveBeenChanged() ? Level.INFO : Level.FINE);
    }

    private void dump(Level level) {
        if (null == LOG) {
            LOG = BatchLogger.getLogger(RefreshStatistics.class.getName());
        }

        LOG.log(level, "Metadata Refresh Statistics", null);
        LOG.log(level, "Movies Created: {0}", created);
        LOG.log(level, "Movies Updated: {0}", updated);
    }

    public void incrementCreated() {
        created++;
    }

    public void incrementUpdated() {
        updated++;
    }

    boolean haveBeenChanged() {
        return created > 0 || updated > 0;
    }
}
