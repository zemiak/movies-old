package com.zemiak.movies.service;

import com.zemiak.movies.domain.*;
import com.zemiak.movies.strings.Encodings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.*;

@Stateless
public class MovieService {
    @PersistenceContext
    private EntityManager em;

    public List<Movie> all() {
        TypedQuery<Movie> query = em.createQuery("SELECT l FROM Movie l ORDER BY l.genreId, l.serieId, l.displayOrder", Movie.class);

        return query.getResultList();
    }

    public List<Movie> getNewMovies() {
        TypedQuery<Movie> query = em.createQuery("SELECT l FROM Movie l WHERE (l.genreId = :genreNew1 OR l.genreId IS NULL) ORDER BY l.genreId, l.serieId, l.displayOrder", Movie.class);
        query.setParameter("genreNew1", em.find(Genre.class, 0));

        return query.getResultList();
    }

    public List<Movie> getSerieMovies(Serie serie) {
        TypedQuery<Movie> query = em.createQuery("SELECT l FROM Movie l WHERE l.serieId IS NULL OR l.serieId = :serie ORDER BY l.genreId, l.serieId, l.displayOrder", Movie.class);
        query.setParameter("serie", serie);

        return query.getResultList();
    }

    public List<Movie> getGenreMovies(Genre genre) {
        TypedQuery<Movie> query = em.createQuery("SELECT l FROM Movie l WHERE l.genreId IS NULL OR l.genreId = :genre ORDER by l.genreId, l.serieId, l.displayOrder", Movie.class);
        query.setParameter("genre", genre);

        return query.getResultList();
    }

    public void save(Movie entity) {
        Movie target = null;

        if (null != entity.getId()) {
            target = em.find(Movie.class, entity.getId());
        }

        if (null == target) {
            em.persist(entity);
        } else {
            target.copyFrom(entity);
        }
    }

    public Movie find(Integer id) {
        return em.find(Movie.class, id);
    }

    public void remove(Integer entityId) {
        em.remove(em.find(Movie.class, entityId));
    }

    public void clearCache(@Observes CacheClearEvent event) {
        em.getEntityManagerFactory().getCache().evictAll();
    }

    public List<Movie> getByExpression(final String text) {
        List<Movie> res = new ArrayList<>();
        String textAscii = Encodings.toAscii(text.trim().toLowerCase());

        all().stream().forEach(entry -> {
            String name = (null == entry.getName() ? ""
                    : Encodings.toAscii(entry.getName().trim().toLowerCase()));
            if (name.contains(textAscii)) {
                res.add(entry);
            }
        });

        return res;
    }

    public List<Movie> getLastMovies(int count) {
        Query query = em.createQuery("SELECT l FROM Movie l ORDER BY l.id DESC");
        query.setMaxResults(count);

        return query.getResultList();
    }

    public List<Movie> findAllNew() {
        List<Movie> res = new ArrayList<>();

        all().stream().filter(movie -> null == movie.getGenreId() || movie.getGenreId().isEmpty()).forEach(movie -> {
            res.add(movie);
        });

        return res;
    }

    public Movie findByFilename(String fileName) {
        Query query = em.createNamedQuery("Movie.findByFileName");
        query.setParameter("fileName", fileName);
        Movie movie;

        try {
            movie = (Movie) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            movie = null;
        }

        return movie;
    }

    public Movie create(String newFile) {
        final Movie movie = new Movie();
        final String baseFileName = new File(newFile).getName();
        final String name = baseFileName.substring(0, baseFileName.lastIndexOf("."));

        movie.setFileName(newFile);
        movie.setGenreId(em.getReference(Genre.class, 0));
        movie.setSerieId(em.getReference(Serie.class, 0));
        movie.setName(name);
        movie.setPictureFileName(name + ".jpg");
        movie.setDisplayOrder(0);
        em.persist(movie);

        return movie;
    }

    public void mergeAndSave(Movie movie) {
        em.merge(movie);
    }

    public void detach(Movie movie) {
        em.detach(movie);
    }

    public void save(Movie bean, Integer genreId, Integer serieId, String languageId, String originalLanguageId, String subtitlesId) {
        bean.setGenreId(em.getReference(Genre.class, genreId));
        bean.setSerieId(em.getReference(Serie.class, serieId));
        bean.setLanguage(em.getReference(Language.class, languageId));
        bean.setOriginalLanguage(em.getReference(Language.class, originalLanguageId));
        bean.setSubtitles(em.getReference(Language.class, subtitlesId));

        save(bean);
    }

    private class Counter {
        private Integer i = 0;

        public Integer get() {
            return i;
        }

        public void inc() {
            this.i++;
        }
    }

    public String getNiceDisplayOrder(Movie movie) {
        final Counter i = new Counter();

        List<Movie> list = em.createQuery("SELECT l FROM Movie l WHERE l.serieId = :serie ORDER BY l.displayOrder", Movie.class)
            .setParameter("serie", movie.getSerieId()).getResultList();
        int count = list.size();
        
        list.stream()
                .peek(m -> {
                    i.inc();
                })
                .filter(m -> m.getId().equals(movie.getId()))
                .findFirst();

        return String.format("%0" + String.valueOf(count).length() + "d", i.get());
    }
}
