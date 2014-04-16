package com.zemiak.movies.batch.thumbnail;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.chunk.ItemProcessor;
import com.zemiak.movies.service.configuration.Configuration;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("ThumbnailsProcessor")
public class Processor implements ItemProcessor {
    private static final Logger LOG = Logger.getLogger(Processor.class.getName());

    @Inject
    private Configuration conf;

    @Override
    public Object processItem(final Object movie) throws Exception {
        final File file = new File((String) movie);
        String imageName = conf.getImgPath() + file.getName();
        imageName = imageName.substring(0, imageName.lastIndexOf(".")) + ".jpg";
        boolean exists = (new File(imageName).exists());

        if (! exists) {
            LOG.log(Level.INFO, "Will generate thumbnail for {0}", movie);
        }

        return (exists ? null : movie);
    }
}
