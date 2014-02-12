package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.batch.service.log.BatchLogger;
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
@Named("MetadataProcessor")
public class Processor implements ItemProcessor {
    private static final BatchLogger LOG = BatchLogger.getLogger(Processor.class.getName());

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
        Movie movie = find(fileName.substring(conf.getProperty("path").length()));
        MovieMetadata data = MetadataReader.read(fileName, movie);
        

        if (null != movie && null != data) {
            if (! data.isMetadataEqual()) {
                return data;
            }
        }
        
        System.out.println("Metadata: skipping " + fileName);

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
}
