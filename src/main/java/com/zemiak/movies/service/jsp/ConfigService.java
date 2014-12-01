package com.zemiak.movies.service.jsp;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.configuration.ConfigBean;

public class ConfigService {
    private final ConfigBean service;

    public ConfigService() {
        CDILookup lookup = new CDILookup();
        service = lookup.lookup(com.zemiak.movies.service.configuration.ConfigBean.class);
    }

    public String getImgServer() {
        return service.getImgServer();
    }
}
