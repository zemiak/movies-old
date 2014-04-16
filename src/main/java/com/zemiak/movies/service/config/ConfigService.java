
package com.zemiak.movies.service.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author vasko
 */
@Singleton
public class ConfigService implements IConfigService {
    @Inject private ConfigServiceGlassfish glassfish;
    @Inject private ConfigServiceWildfly wildfly;
    private IConfigService impl;
    
    @PostConstruct
    public void init() {
        impl = null == wildfly.getPath() ? glassfish : wildfly;
        
        if (null == wildfly.getPath()) {
            System.err.println("Read configuration from Glassfish server.");
        } else {
            System.err.println("Read configuration from Wildfly server.");
        }
    }

    @Override
    public String getBackendUrl() {
        return impl.getBackendUrl();
    }

    @Override
    public String getFfmpeg() {
        return impl.getFfmpeg();
    }

    @Override
    public String getImgPath() {
        return impl.getImgPath();
    }

    @Override
    public String getMailFrom() {
        return impl.getMailFrom();
    }

    @Override
    public String getMailSubject() {
        return impl.getMailSubject();
    }

    @Override
    public String getMailTo() {
        return impl.getMailTo();
    }

    @Override
    public String getMp4info() {
        return impl.getMp4info();
    }

    @Override
    public String getMp4tags() {
        return impl.getMp4tags();
    }

    @Override
    public String getPath() {
        return impl.getPath();
    }
}
