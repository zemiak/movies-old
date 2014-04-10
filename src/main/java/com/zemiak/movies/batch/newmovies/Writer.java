package com.zemiak.movies.batch.newmovies;

import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Named("NewmoviesWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Override
    public void writeItems(final List list) throws Exception {
        for (Object obj: list) {
            final String newFile = (String) obj;

            Movie m = createMovie(newFile);
            em.persist(m);
            em.flush();

            LOG.log(Level.INFO, "Created a new movie ''{0}''/''{1}''...",
                    new Object[]{m.getFileName(), m.getName()});
        }
    }

    private Movie createMovie(final String newFile) {
        final Movie movie = new Movie();
        final String baseFileName = new File(newFile).getName();
        final String name = baseFileName.substring(0, baseFileName.lastIndexOf("."));

        movie.setFileName(newFile);
        movie.setGenreId(em.getReference(Genre.class, 0));
        movie.setSerieId(em.getReference(Serie.class, 0));
        movie.setName(name);
        movie.setPictureFileName(name + ".jpg");

        return movie;
    }
}
