package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.service.log.BatchLogger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PrepareFileList")
public class PrepareFileList implements Batchlet {
    @Inject
    private JobContext jobCtx;

    @Inject
    private String path;

    private final List<String> files = new ArrayList<>();

    private static final BatchLogger LOG = BatchLogger.getLogger(PrepareFileList.class.getName());
    private static final Logger LOG1 = Logger.getLogger(PrepareFileList.class.getName());


    public PrepareFileList() {
    }

    @Override
    public String process() throws Exception {
        String fileList = getFileListName();
        long counter = 0;

        File mainDir = new File(path);
        if (! mainDir.isDirectory()) {
            throw new IOException(path + " is not a directory");
        }

        if (! mainDir.canExecute() || ! mainDir.canRead()) {
            throw new IOException(path + " is not readable");
        }

        try (FileWriter stream = new FileWriter(fileList)) {
            final String newLine = System.getProperty("line.separator");

            readMovieFiles(mainDir);

            for (String name: files) {
                stream.write(name + newLine);
                counter++;
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot open " + fileList + " for writing.", ex);
            throw ex;
        }

        LOG1.log(Level.INFO, "Found {0} movies on HDD.", counter);

        return "done";
    }

    @Override
    public void stop() throws Exception {
        File file = new File(getFileListName());
        file.delete();
    }

    private String getFileListName() {
        return jobCtx.getProperties().getProperty("fileList").replace("/", RemoveFileList.PATH_SEPARATOR);
    }

    private void readMovieFiles(final File mainDir) {
        for (String fileName : mainDir.list()) {
            final File file = new File(mainDir.getAbsolutePath() + RemoveFileList.PATH_SEPARATOR + fileName);

            if ((file.isDirectory()) && (! fileName.startsWith("."))) {
                readMovieFiles(file);
            } else {
                processFile(file.getAbsolutePath());
            }
        }
    }

    private void processFile(final String absolutePath) {
        final int extPos = absolutePath.lastIndexOf(".");
        final String ext = absolutePath.substring(extPos + 1, absolutePath.length()).toLowerCase();

        if ("mp4".equals(ext) || "m4v".equals(ext)) {
            files.add(absolutePath);
        }
    }
}
