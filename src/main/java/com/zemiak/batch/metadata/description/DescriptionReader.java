package com.zemiak.batch.metadata.description;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class DescriptionReader {
    @Inject private Csfd csfd;
    @Inject private Imdb imdb;
    
    public String read(final String url) {
        if (null == url || url.trim().isEmpty()) {
            return null;
        }
        
        if (csfd.acceptsUrl(url)) {
            return csfd.getDescription(url);
        }
        
        if (imdb.acceptsUrl(url)) {
            return imdb.getDescription(url);
        }
        
        return null;
    }
}
