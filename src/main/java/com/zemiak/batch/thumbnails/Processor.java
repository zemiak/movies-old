package com.zemiak.batch.thumbnails;

import java.util.logging.Logger;
import javax.batch.api.chunk.ItemProcessor;

/**
 *
 * @author vasko
 */
public class Processor implements ItemProcessor {
    private static final Logger LOG = Logger.getLogger(Processor.class.getName());
    
    @Override
    public Object processItem(Object book) throws Exception {
        String fileName = (String) book;
        

        return null;
    }
}
