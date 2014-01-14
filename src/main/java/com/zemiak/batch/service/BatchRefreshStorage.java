package com.zemiak.batch.service;

import com.zemiak.movies.domain.CacheClearEvent;
import javax.batch.api.Batchlet;
import javax.inject.Inject;
import javax.inject.Named;

@Named("BatchRefreshStorage")
public class BatchRefreshStorage implements Batchlet {
    @Inject 
    private javax.enterprise.event.Event<CacheClearEvent> clearEvent;

    @Override
    public String process() throws Exception {
        // clear Storage cache
        clearEvent.fire(new CacheClearEvent());
        
        return "cleared-cache";
    }

    @Override
    public void stop() throws Exception {
    }
}
