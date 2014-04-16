package com.zemiak.movies.service.config;

import com.zemiak.movies.lookup.CDILookup;

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
