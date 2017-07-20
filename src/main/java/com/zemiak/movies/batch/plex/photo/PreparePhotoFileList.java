package com.zemiak.movies.batch.plex.photo;

import com.zemiak.movies.service.ConfigurationProvider;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PreparePhotoFileList {
    @Inject
    ConfigurationProvider config;

    private final List<String> files = new ArrayList<>();
    private final static Pattern yearPattern = Pattern.compile("^20\\d\\d$");

    private static final Logger LOG = Logger.getLogger(PreparePhotoFileList.class.getName());


    @PostConstruct
    public void init() {
        File mainDir = new File(photoPath);
        if (! mainDir.isDirectory()) {
            LOG.log(Level.SEVERE, photoPath + " is not a directory");
            return;
        }

        if (! mainDir.canExecute() || ! mainDir.canRead()) {
            LOG.log(Level.SEVERE, photoPath + " is not readable");
            return;
        }

        readPhotoFiles(mainDir);

        LOG.log(Level.FINE, "Found {0} year photo folders on HDD.", files.size());
    }

    public List<String> getFiles() {
        return files;
    }

    private void readPhotoFiles(final File mainDir) {
        for (String fileName : mainDir.list()) {
            final File file = Paths.get(mainDir.getAbsolutePath(), fileName).toFile();

            if ((file.isDirectory()) && (yearPattern.matcher(fileName).matches())) {
                files.add(fileName);
            }
        }
    }
}
