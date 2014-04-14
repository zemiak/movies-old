package com.zemiak.movies.boundary.models;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.ConfigService;

public class ConfigBean {
    private ConfigService conf;

    public ConfigBean() {
        conf = new CDILookup().lookup(ConfigService.class);
    }

    public String getBackendUrl() {
        return conf.getBackendUrl();
    }

    public String getPath() {
        return conf.getPath();
    }
}
