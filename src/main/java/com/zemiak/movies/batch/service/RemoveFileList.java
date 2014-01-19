package com.zemiak.movies.batch.service;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

/**
 *
 * @author vasko
 */
public class RemoveFileList implements Batchlet {
    @Inject
    JobContext jobCtx;
    
    final static public String PATH_SEPARATOR = System.getProperty("file.separator");

    private static final Logger LOG = Logger.getLogger(RemoveFileList.class.getName());
    
    public RemoveFileList() {
    }

    @Override
    public String process() throws Exception {
        String fileList = getFileListName();
        
        File file = new File(fileList);
        if (! file.delete()) {
            LOG.log(Level.SEVERE, "Cannot remove temp file: {0}", fileList);
            return "delete-error";
        }
        
        return "ok";
    }
    
    private String getFileListName() {
        return jobCtx.getProperties().getProperty("fileList").replace("/", PATH_SEPARATOR);
    }

    @Override
    public void stop() throws Exception {
    }
}