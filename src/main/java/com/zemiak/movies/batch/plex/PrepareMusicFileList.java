package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.service.BatchLogger;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PrepareMusicFileList {
    @Inject
    private String musicPath;

    private final List<String> files = new ArrayList<>();

    private static final BatchLogger LOG = BatchLogger.getLogger(PrepareMusicFileList.class.getName());
    private static final Logger LOG1 = Logger.getLogger(PrepareMusicFileList.class.getName());


    @PostConstruct
    public void init() {
        long counter = 0;

        File mainDir = new File(musicPath);
        if (! mainDir.isDirectory()) {
            LOG.log(Level.SEVERE, musicPath + " is not a directory", null);
            return;
        }

        if (! mainDir.canExecute() || ! mainDir.canRead()) {
            LOG.log(Level.SEVERE, musicPath + " is not readable", null);
            return;
        }

        readMusicFiles(mainDir);

        LOG1.log(Level.INFO, "Found {0} movies on HDD.", counter);
    }

    public List<String> getFiles() {
        return files;
    }

    private void readMusicFiles(final File mainDir) {
        for (String fileName : mainDir.list()) {
            final File file = Paths.get(mainDir.getAbsolutePath(), fileName).toFile();

            if ((file.isDirectory()) && (! fileName.startsWith("."))) {
                readMusicFiles(file);
            } else {
                processFile(file.getAbsolutePath());
            }
        }
    }

    private void processFile(final String absolutePath) {
        final int extPos = absolutePath.lastIndexOf(".");
        final String ext = absolutePath.substring(extPos + 1, absolutePath.length()).toLowerCase();

        if ("m4a".equals(ext)) {
            files.add(absolutePath);
        }
    }
}
