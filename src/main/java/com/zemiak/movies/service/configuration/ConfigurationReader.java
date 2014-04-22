package com.zemiak.movies.service.configuration;

import com.zemiak.movies.lookup.CustomResourceLookup;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * WFLY is behaving strange when injecting custom resources.
 */
@Singleton
@Startup
public class ConfigurationReader {
    final CustomResourceLookup lookupService = new CustomResourceLookup();
    private String prefix;
    
    @Inject Configuration config;
    
    @PostConstruct
    public void init() {
        String path = new CustomResourceLookup().lookup("java:/jboss/com.zemiak.movies.path");
        
        System.err.println("JBoss Path: '" + path + "'");
        
        if (null != path) {
            read("java:/jboss/");
        } else {
            path = new CustomResourceLookup().lookup("com.zemiak.movies.path");
            System.err.println("Glassfish Path: '" + path + "'");
            read("");
        }
    }

    private void read(String prefix) {
        this.prefix = prefix + "com.zemiak.movies.";
        
        config.setBackendUrl(readStringProperty("backendUrl"));
        config.setFfmpeg(readStringProperty("ffmpeg"));
        config.setImgPath(readStringProperty("imgPath"));
        config.setMailFrom(readStringProperty("mailFrom"));
        config.setMailSubject(readStringProperty("mailSubject"));
        config.setMailTo(readStringProperty("mailTo"));
        config.setMp4info(readStringProperty("mp4info"));
        config.setMp4tags(readStringProperty("mp4tags"));
        config.setPath(readStringProperty("path"));
    }
    
    private String readStringProperty(String name) {
        return lookupService.lookup(prefix + name);
    }
}
