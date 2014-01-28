package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.domain.Movie;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Named("MetadataProcessor")
public class Processor implements ItemProcessor {
    private static final Logger LOG = Logger.getLogger(Processor.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Resource(name = "com.zemiak.movies")
    private Properties conf;
    
    private Query query;
    
    @PostConstruct
    public void init() {
        query = em.createNamedQuery("Movie.findByFileName");
    }
    
    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        MovieMetadata data = MetadataReader.read(fileName);
        Movie movie = find(fileName.substring(conf.getProperty("path").length()));
        
        if (null != movie && null != data) {
            if (! isMetadataEqual(movie, data)) {
                return movie;
            }
        }

        return null;
    }
    
    public Movie find(final String fileName) {
        Movie movie;
        
        query.setParameter("fileName", fileName);
        
        try {
            movie = (Movie) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
        
        return movie;
    }

    private boolean isMetadataEqual(Movie movie, MovieMetadata data) {
        if (null == data.getGenre() || null == data.getName()) {
            LOG.info("isMetadataEqual: Genre or name is null");
            return false;
        }
        
        if (! data.getGenre().equals(movie.composeGenreName())) {
            LOG.info("isMetadataEqual: Genre is not equal");
            return false;
        }
        
        if (! data.getName().equals(movie.getName())) {
            LOG.info("isMetadataEqual: Name is not equal");
            return false;
        }
        
        boolean commentsAreEmpty = (data.getComments() == null
                || data.getComments().trim().isEmpty() 
                || "''".equals(data.getComments()));
        
        if (commentsAreEmpty) {
            LOG.info("isMetadataEqual: Comments are empty");
        }
        
        return !commentsAreEmpty;
    }
}
