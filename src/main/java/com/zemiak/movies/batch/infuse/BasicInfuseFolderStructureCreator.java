package com.zemiak.movies.batch.infuse;

import com.zemiak.movies.service.ConfigurationProvider;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;

@Dependent
public class BasicInfuseFolderStructureCreator {
    private static final Logger LOG = Logger.getLogger(BasicInfuseFolderStructureCreator.class.getName());

    private final String infuseLinkPath = ConfigurationProvider.getInfuseLinkPath();

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
            Files.createDirectories(Paths.get(infuseLinkPath));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Basic Infuse folder creation error", ex);
        }
    }
}
