package com.zemiak.movies.batch.thumbnail;

import com.zemiak.movies.batch.service.CommandLine;
import com.zemiak.movies.batch.service.log.BatchLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("ThumbnailsWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Resource(name = "com.zemiak.movies")
    private Properties conf;

    @Override
    public void writeItems(List list) throws Exception {
        for (Object obj : list) {
            final File file = new File((String) obj);
            final String movieFileName = file.getAbsolutePath();
            final int offset = new Random().nextInt(200) + 300;

            String imageFileName = conf.getProperty("imgPath") + file.getName();
            final int pos = imageFileName.lastIndexOf(".");

            imageFileName = imageFileName.substring(0, pos) + ".jpg";

            final List<String> params = Arrays.asList(new String[]{
                "-s", "220", "-i", movieFileName, "-o", imageFileName
            });
            
            try {
                CommandLine.execCmd(conf.getProperty("ffmpeg"), params);
                LOG.log(Level.INFO, "Generated thumbnail {0} ...", imageFileName);
            } catch (IllegalStateException ex) {
                LOG.log(Level.SEVERE, "DID NOT generate thumbnail {0}: {1} ...", 
                        new Object[]{imageFileName, ex.getMessage()});
            }            
        }
    }
}
