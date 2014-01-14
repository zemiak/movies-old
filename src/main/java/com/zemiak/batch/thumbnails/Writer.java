package com.zemiak.batch.thumbnails;

import java.util.List;
import java.util.logging.Logger;
import javax.batch.api.chunk.AbstractItemWriter;

/**
 *
 * @author vasko
 */
public class Writer extends AbstractItemWriter {

    private static final Logger LOG = Logger.getLogger(Writer.class.getName());

    @Override
    public void writeItems(List list) throws Exception {
        for (Object obj : list) {
            String fileName = (String) obj;
        }
    }
}
