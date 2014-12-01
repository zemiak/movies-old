package com.zemiak.movies.service.configuration;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.lookup.CustomResourceLookup;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named("configBean")
public class ConfigBean implements Serializable {
    CustomResourceLookup lookup;
    @Inject Configuration conf;

    public ConfigBean() {
        conf = new CDILookup().lookup(com.zemiak.movies.service.configuration.Configuration.class);
        lookup = new CustomResourceLookup();
    }

    @PostConstruct
    public void init() {
        System.err.println("init(): Configuration: '" + conf + "'");
    }

    public String getBackendUrl() {
        return null == conf ? (String) lookup.lookup("com.zemiak.movies.backendUrl") : conf.getBackendUrl();
    }

    public String getImgServer() {
        return null == conf ? (String) lookup.lookup("com.zemiak.movies.imgServer") : conf.getImgServer();
    }

    public String getPlayServer() {
        return null == conf ? (String) lookup.lookup("com.zemiak.movies.playServer") : conf.getPlayServer();
    }

    public String getPath() {
        return null == conf ? (String) lookup.lookup("com.zemiak.movies.path") : conf.getPath();
    }
}
