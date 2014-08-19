package com.zemiak.movies.service.yaml;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Stateless
public class Dumper {
    private static final Logger LOG = Logger.getLogger(Dumper.class.getName());
    private static final String FILE_NAME = "/tmp/dev-data.yml";

    @PersistenceContext
    EntityManager em;
    

    public void dump() {
        List<Object> entities = new ArrayList<>();

        loadGenres(entities);
        loadSeries(entities);
        loadLanguages(entities);
        loadMovies(entities);

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);

        final Yaml yaml = new Yaml(options);
        final String dumped = yaml.dump(entities);

        try (OutputStream os = new FileOutputStream(FILE_NAME)) {
            System.err.println("Dumped data to: " + os.toString());
            os.write(dumped.getBytes());
            os.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "File save problem", ex);
        }
    }


    private void loadGenres(List<Object> entities) {
        em.createNamedQuery("Genre.findAll", Genre.class).getResultList().stream().forEach((item) -> {
            entities.add(item);
        });
    }

    private void loadSeries(List<Object> entities) {
        em.createNamedQuery("Serie.findAll", Serie.class).getResultList().stream().forEach((item) -> {
            entities.add(item);
        });
    }

    private void loadLanguages(List<Object> entities) {
        em.createNamedQuery("Language.findAll", Language.class).getResultList().stream().forEach((item) -> {
            entities.add(item);
        });
    }

    private void loadMovies(List<Object> entities) {
        em.createNamedQuery("Movie.findAll", Movie.class).getResultList().stream().forEach((_item) -> {
            entities.add(_item);
        });
    }
}