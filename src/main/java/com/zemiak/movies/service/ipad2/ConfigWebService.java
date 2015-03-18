package com.zemiak.movies.service.ipad2;

import com.zemiak.movies.lookup.CDILookup;
import com.zemiak.movies.service.configuration.ConfigBean;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named("configWebService")
public class ConfigWebService implements Serializable {
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
