package com.zemiak.batch.thumbnails;

import com.zemiak.batch.service.CommandLine;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("ThumbnailsWriter")
public class Writer extends AbstractItemWriter {
    private static final Logger LOG = Logger.getLogger(Writer.class.getName());
    
    private static final String FFMPEG="ffmpeg -itsoffset -%d -i \"%s\" -vcodec mjpeg"
            + " -vframes 1 -an -f rawvideo -s 220x160 \"%s\"";
    
    @Resource(name = "com.zemiak.movies")
    private Properties conf;

    @Override
    public void writeItems(List list) throws Exception {
        for (Object obj : list) {
            final File file = new File((String) obj);
            final String movieFileName = file.getAbsolutePath();
            final int offset = new Random().nextInt(400) + 240;
            
            String imageFileName = conf.getProperty("imgPath") + file.getName();
            final int pos = imageFileName.lastIndexOf(".");
            
            imageFileName = imageFileName.substring(0, pos) + ".jpg";
            
            final String command = String.format(FFMPEG, offset, movieFileName, imageFileName);
            
            CommandLine.execCmd(command);
        }
    }
}
