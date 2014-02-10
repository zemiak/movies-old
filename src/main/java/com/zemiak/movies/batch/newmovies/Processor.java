package com.zemiak.movies.batch.newmovies;

import com.zemiak.movies.batch.service.RemoveFileList;
import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.batch.service.log.LoggerInstance;
import com.zemiak.movies.domain.Movie;
import java.util.Properties;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author vasko
 */
@Named("NewmoviesProcessor")
public class Processor implements ItemProcessor {
    private static final LoggerInstance LOG = BatchLogger.getLogger(Processor.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Resource(name = "com.zemiak.movies")
    private Properties conf;
    
    private String prefix;
    private Query query;
    
    @PostConstruct
    public void init() {
        prefix = conf.getProperty("path");
        query = em.createNamedQuery("Movie.findByFileName");
    }
    
    @Override
    public Object processItem(final Object movie) throws Exception {
        final String fileName = (String) movie;
        if (! exists(getRelativeFilename(fileName))) {
            LOG.log(Level.INFO, "Found a new file: {0}", fileName);
            return fileName;
        }

        return null;
    }
    
    private String getRelativeFilename(final String absoluteFilename) {
        String absoluteWithSlashes = absoluteFilename.replaceAll("\\" + RemoveFileList.PATH_SEPARATOR, "/");
        if (absoluteWithSlashes.startsWith(prefix)) {
            absoluteWithSlashes = absoluteWithSlashes.substring(prefix.length());
        }
        
        return absoluteWithSlashes;
    }
    
    private boolean exists(final String relativeFilename) {
        Movie movie;
        
        query.setParameter("fileName", relativeFilename);
        
        try {
            movie = (Movie) query.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        
        return (null != movie);
    }
}
