package com.zemiak.movies.batch.thumbnail;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("ThumbnailsProcessor")
public class Processor implements ItemProcessor {
    private static final Logger LOG = Logger.getLogger(Processor.class.getName());
    
    @Resource(name = "com.zemiak.movies")
    private Properties conf;
    
    @Override
    public Object processItem(final Object movie) throws Exception {
        final File file = new File((String) movie);
        String imageName = conf.getProperty("imgPath") + file.getName();
        imageName = imageName.substring(0, imageName.lastIndexOf(".")) + ".jpg";
        
        return ((new File(imageName).exists()) ? null : movie);
    }
}
