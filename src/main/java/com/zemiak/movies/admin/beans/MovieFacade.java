package com.zemiak.movies.admin.beans;

import com.zemiak.movies.domain.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vasko
 */
@Stateless
public class MovieFacade extends AbstractFacade<Movie> {
    @PersistenceContext(unitName = "ZemiakMovies")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MovieFacade() {
        super(Movie.class);
    }

    public List<Movie> findAllNew() {
        List<Movie> res = new ArrayList<>();
        
        for (Movie movie: findAll()) {
            if (null == movie.getGenreId() || movie.getGenreId().isEmpty()) {
                res.add(movie);
            }
        }
        
        return res;
    }
    
}
