package com.zemiak.movies.batch.plex.music;

import java.nio.file.Files;
import java.nio.file.Paths;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("BasicMusicFolderStructureCreator")
public class BasicMusicFolderStructureCreator implements Batchlet {
    @Inject
    private JobContext jobCtx;

    @Override
    public String process() throws Exception {
        String plexFolder = jobCtx.getProperties().getProperty("plexFolder");
        Files.createDirectories(Paths.get(plexFolder, MusicWriter.PATH));

        return "ok";
    }

    @Override
    public void stop() throws Exception {
    }
}
