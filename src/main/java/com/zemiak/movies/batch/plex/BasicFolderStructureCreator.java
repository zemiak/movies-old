package com.zemiak.movies.batch.plex;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("FilenameItemReader")
public class BasicFolderStructureCreator implements Batchlet {
    private static final Logger LOG = Logger.getLogger(BasicFolderStructureCreator.class.getName());

    @Inject
    private JobContext jobCtx;

    @Override
    public String process() throws Exception {
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

        Files.createDirectories(Paths.get(plexFolder, MovieWriter.PATH));
        Files.createDirectories(Paths.get(plexFolder, SerieItemWriter.PATH));

        return "ok";
    }

    @Override
    public void stop() throws Exception {
    }
}
