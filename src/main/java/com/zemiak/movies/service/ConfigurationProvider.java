package com.zemiak.movies.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@Singleton
@Startup
public class ConfigurationProvider {
    private static final Logger LOG = Logger.getLogger(ConfigurationProvider.class.getName());
    
    private final Map<String, String> configuration = new HashMap<>();

    @PostConstruct
    public void readConfiguration() {
	ResourceBundle props = ResourceBundle.getBundle("config");
        
        for (String key: props.keySet()) {
            configuration.put(key, props.getString(key));
        }
    }
    
    @Produces
    public String getString(InjectionPoint point) {
        String fieldName = point.getMember().getName();
        String valueForFieldName = configuration.get(fieldName);
        return valueForFieldName;
    }
    
    @Produces
    public int getInt(InjectionPoint point) {
        String stringValue = getString(point);
        if (stringValue == null) {
            return 0;
        }
        
        return Integer.parseInt(stringValue);
    }
}
