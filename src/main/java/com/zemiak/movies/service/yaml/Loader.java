package com.zemiak.movies.service.yaml;

import com.zemiak.movies.service.configuration.Configuration;
import java.util.Collection;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.yaml.snakeyaml.Yaml;

@Singleton
@Startup
@DependsOn("ConfigurationReader")
public class Loader {
    @PersistenceContext
    EntityManager em;

    @Inject
    Configuration conf;

    public Loader() {
    }

    @PostConstruct
    public void init() {
        if (conf.isDevelopmentSystem()) {
            load("/dev-data.yml");
        }
    }

    private void load(final String fileName) {
        Yaml yaml = new Yaml();
        for (Object obj : yaml.loadAll(Loader.class.getResourceAsStream(fileName))) {
            persist(obj);
        }
    }

    private void persist(Object obj) {
        if (obj instanceof Collection) {
            ((Collection) obj).stream().forEach((object) -> {
                persist(object);
            });
        } else if (obj instanceof Map) {
            ((Map) obj).values().stream().forEach((object) -> {
                persist(object);
            });

        } else {
            em.persist(obj);
        }
    }
}
