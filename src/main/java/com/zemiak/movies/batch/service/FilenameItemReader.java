package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.service.log.BatchLogger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.logging.Level;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vasko
 */
@Named("FilenameItemReader")
public class FilenameItemReader extends AbstractItemReader {
    private static final BatchLogger LOG = BatchLogger.getLogger(FilenameItemReader.class.getName());

    @Inject
    JobContext jobCtx;

    FileInputStream inputStream;
    BufferedReader br;
    int recordNumber;

    public FilenameItemReader() {
    }

    @Override
    public void open(Serializable prevCheckpointInfo) throws IOException {
        String fileList = jobCtx.getProperties().getProperty("fileList");

        inputStream = new FileInputStream(fileList);
        br = new BufferedReader(new InputStreamReader(inputStream));

        if (prevCheckpointInfo != null) {
            recordNumber = (Integer) prevCheckpointInfo;
        }

        for (int i = 1; i < recordNumber; i++) {   //Skip upto recordNumber
            br.readLine();
        }

        System.out.println("Opened file list from record: " + recordNumber);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return recordNumber;
    }

    @Override
    public Object readItem() throws IOException {
        String line = br.readLine();

        return (null == line) ? null : line.trim();
    }
}
