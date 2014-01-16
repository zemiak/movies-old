package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.metadata.domain.MovieMetadata;
import com.zemiak.movies.batch.metadata.domain.MetadataReader;
import com.zemiak.movies.domain.Movie;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
    
    private Query query;
    
    @PostConstruct
    public void init() {
        query = em.createNamedQuery("Movie.findByFileName");
    }
    
    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        MovieMetadata data = MetadataReader.read(fileName);
        Movie movie = find(fileName);
        
        if (null != movie && null != data) {
            if (! isMetadataEqual(movie, data)) {
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
        
        return data.getName().equals(movie.getName());
    }
}
