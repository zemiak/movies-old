package com.zemiak.movies.batch.plex;

import com.zemiak.movies.service.MovieService;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("PlexProcessor")
public class Processor implements ItemProcessor {
    private static final Logger LOG = Logger.getLogger(Processor.class.getName());

    @Inject private MovieService service;
    @Inject private String path;
    @Inject private JobContext jobCtx;

    @PostConstruct
    public void init() {
        String plexFolder = jobCtx.getProperties().getProperty("plexFolder");
        Path directory = Paths.get(plexFolder);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error deleting plex folder " + plexFolder, ex);
        }

    }


    @Override
    public Object processItem(final Object movieName) throws Exception {
        final String fileName = (String) movieName;
        return service.findByFilename(fileName.substring(path.length()));
    }
}
