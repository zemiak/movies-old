package com.zemiak.movies.servlet;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ServletConfiguration {
    @Inject
    private String path;
    
    @Inject
    private String imgPath;
    
    public String getPath() {
        return path;
    }
    
    public String getImgPath() {
        return imgPath;
    }
}
