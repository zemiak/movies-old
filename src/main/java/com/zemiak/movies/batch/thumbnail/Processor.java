package com.zemiak.movies.batch.thumbnail;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.service.MovieService;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("ThumbnailsProcessor")
public class Processor implements ItemProcessor {
    private static final Logger LOG = Logger.getLogger(Processor.class.getName());

    @Inject MovieService service;
    @Inject String path;
    @Inject String imgPath;

    @Override
    public Object processItem(final Object movieFileName) throws Exception {
        final File file = new File((String) movieFileName);
        final String fileName = file.getAbsolutePath();
        final Movie movie = service.findByFilename(fileName.substring(path.length()));

        String imageName = imgPath + file.getName();
        imageName = imageName.substring(0, imageName.lastIndexOf(".")) + ".jpg";
        boolean exists = (new File(imageName).exists());

        if (! exists) {
            LOG.log(Level.INFO, "Will generate thumbnail for {0}", movie);
        }

        return (exists ? null : movie);
    }
}
