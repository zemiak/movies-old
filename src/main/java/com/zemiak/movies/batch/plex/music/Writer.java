package com.zemiak.movies.batch.plex.music;

import com.zemiak.movies.batch.service.log.BatchLogger;
import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PlexMusicWriter")
public class Writer extends AbstractItemWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(Writer.class.getName());

    @Inject private JobContext jobCtx;
    @Inject private MusicWriter musicWriter;

    @Override
    public void writeItems(final List list) throws Exception {
        String plexFolder = jobCtx.getProperties().getProperty("plexFolder");
        list.stream().filter(obj -> null != obj).forEach(obj -> {
            String musicFileName = (String) obj;
            musicWriter.process(plexFolder, musicFileName);
        });
    }
}
