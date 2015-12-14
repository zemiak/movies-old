package com.zemiak.movies.batch.infuse;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class BasicInfuseFolderStructureCreator {
    private static final Logger LOG = Logger.getLogger(BasicInfuseFolderStructureCreator.class.getName());

    @Inject String infuseLinkPath;

    public void cleanAndCreate() {
        Path directory = Paths.get(infuseLinkPath);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (infuseLinkPath.equals(dir.toString())) {
                        return FileVisitResult.CONTINUE;
                    }

                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error deleting Infuse folder " + infuseLinkPath, ex);
        }

        try {
            Files.createDirectories(Paths.get(infuseLinkPath, InfuseMovieWriter.PATH));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Basic Infuse folder creation error", ex);
        }
    }
}
