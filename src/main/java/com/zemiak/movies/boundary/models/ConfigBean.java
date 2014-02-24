package com.zemiak.movies.boundary.models;

import com.zemiak.movies.lookup.CustomResourceLookup;
import java.util.Properties;

public class ConfigBean {
    private Properties conf;
    
    public ConfigBean() {
        conf = new CustomResourceLookup().lookup("com.zemiak.movies");
    }
    
    public String getBackendUrl() {
        return conf.getProperty("backendUrl");
    }
}
