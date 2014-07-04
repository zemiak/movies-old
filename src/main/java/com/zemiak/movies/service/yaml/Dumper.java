package com.zemiak.movies.service.yaml;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Dumper {
    @PersistenceContext
    EntityManager em;

    public String dump() {
        List<Object> entities = new ArrayList<>();

        loadGenres(entities);
        loadSeries(entities);
        loadLanguages(entities);
        loadMovies(entities);

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);

        Yaml yaml = new Yaml(options);
        return yaml.dump(entities);
    }

    private void loadGenres(List<Object> entities) {
        em.createNamedQuery("Genre.findAll", Genre.class).getResultList().stream().forEach((_item) -> {
            entities.add(_item);
        });
    }

    private void loadSeries(List<Object> entities) {
        em.createNamedQuery("Serie.findAll", Serie.class).getResultList().stream().forEach((_item) -> {
            entities.add(_item);
        });
    }

    private void loadLanguages(List<Object> entities) {
        em.createNamedQuery("Language.findAll", Language.class).getResultList().stream().forEach((_item) -> {
            entities.add(_item);
        });
    }

    private void loadMovies(List<Object> entities) {
        em.createNamedQuery("Movie.findAll", Movie.class).getResultList().stream().forEach((_item) -> {
            entities.add(_item);
        });
    }
}
