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
        System.err.println("MetadataProcessor.init.begin");
        query = em.createNamedQuery("Movie.findByFileName");
        System.err.println("MetadataProcessor.init.end");
    }
    
    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        MovieMetadata data = MetadataReader.read(fileName);
        Movie movie = find(fileName.substring(conf.getProperty("path").length()));
        
        if (null != movie && null != data) {
            if (! isMetadataEqual(movie, data)) {
                LOG.log(Level.INFO, "... MetadataProcessor: Going to update metadata {0}", 
                        new Object[]{fileName});
                LOG.log(Level.INFO, "..... Metadata {0}, Movie {1}, current Genre {2}", 
                        new Object[]{data, movie, movie.composeGenreName()});
                return fileName;
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
            return false;
        }
        
        if (! data.getGenre().equals(movie.composeGenreName())) {
            return false;
        }
        
        if (! data.getName().equals(movie.getName())) {
            return false;
        }
        
        return (data.getComments() != null && 
                (data.getComments().trim().isEmpty() || "''".equals(data.getComments())));
    }
}
