package com.zemiak.movies.batch.service;

import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RefreshStatistics {
    private BatchLogger LOG = null;

    private int created;
    private int updated;
    private int linksCreated;
    private int missingMusicMetadata;

    @Inject Boolean developmentSystem;

    @PostConstruct
    public void reset() {
        created = updated = linksCreated = missingMusicMetadata = 0;
    }

    public void dump() {
        dump(developmentSystem || created > 0 || updated > 0 ? Level.INFO : Level.FINE);
    }

    private void dump(Level level) {
        if (null == LOG) {
            LOG = BatchLogger.getLogger(RefreshStatistics.class.getName());
        }

        LOG.log(level, "Metadata Refresh Statistics", null);
        LOG.log(level, "Movies Created: {0}", created);
        LOG.log(level, "Movies Updated: {0}", updated);
        LOG.log(level, "3rd Party Links Created: {0}", linksCreated);
        LOG.log(level, "Music Files w/out Metadata: {0}", missingMusicMetadata);
    }

    public void incrementCreated() {
        created++;
    }

    public void incrementUpdated() {
        updated++;
    }

    public void incrementLinksCreated() {
        linksCreated++;
    }

    public void incrementMissingMusicMetadata() {
        missingMusicMetadata++;
    }
}
