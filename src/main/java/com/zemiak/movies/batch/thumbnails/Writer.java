package com.zemiak.movies.batch.thumbnails;

import com.zemiak.movies.batch.service.CommandLine;
import java.io.File;
import java.util.ArrayList;
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
            
            final List<String> params = new ArrayList<>();
            
            params.add("-itsoffset");
            params.add(String.valueOf(offset));
            
            params.add("-i");
            params.add(movieFileName);
            
            params.add("-vcodec");
            params.add("mjpeg");
            
            params.add("-vframes");
            params.add("1");
            
            params.add("-an");
            
            params.add("-f");
            params.add("rawvideo");
            
            params.add("-s");
            params.add("220x160");
            
            params.add(imageFileName);
            
            CommandLine.execCmd(conf.getProperty("ffmpeg"), params);
        }
    }
}
