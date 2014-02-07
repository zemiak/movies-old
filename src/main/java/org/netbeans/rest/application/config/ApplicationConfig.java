package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author vasko
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.zemiak.movies.boundary.backbonerest.GenreBackbone.class);
        resources.add(com.zemiak.movies.boundary.backbonerest.LanguageBackbone.class);
        resources.add(com.zemiak.movies.boundary.backbonerest.MovieBackbone.class);
        resources.add(com.zemiak.movies.boundary.backbonerest.SerieBackbone.class);
    }
    
}
