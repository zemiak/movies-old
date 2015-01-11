package com.zemiak.movies.service.jsp;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.configuration.ConfigBean;

public class ConfigWebService {
    private final ConfigBean service;

    public ConfigWebService() {
        CDILookup lookup = new CDILookup();
        service = lookup.lookup(com.zemiak.movies.service.configuration.ConfigBean.class);
    }

    public String getImgServer() {
        return service.getImgServer();
    }

    public String getPlayServer() {
        return service.getPlayServer();
    }
}
