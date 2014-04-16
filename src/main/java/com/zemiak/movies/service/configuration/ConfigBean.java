package com.zemiak.movies.service.configuration;

import com.zemiak.movies.lookup.CDILookup;

public class ConfigBean {
    private Configuration conf;

    public ConfigBean() {
        conf = new CDILookup().lookup(Configuration.class);
    }

    public String getBackendUrl() {
        return conf.getBackendUrl();
    }

    public String getPath() {
        return conf.getPath();
    }
}
