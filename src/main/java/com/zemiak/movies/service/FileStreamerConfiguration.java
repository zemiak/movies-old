package com.zemiak.movies.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class FileStreamerConfiguration {
    @Inject
    private String path;
    
    public String getPath() {
        return path;
    }
}
