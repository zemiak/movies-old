package com.zemiak.movies.batch.plex;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class BasicPlexFolderStructureCreator {
    private static final Logger LOG = Logger.getLogger(BasicPlexFolderStructureCreator.class.getName());

    @Inject String plexPath;

    public void cleanAndCreate() {
        Path directory = Paths.get(plexPath);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (plexPath.equals(dir.toString())) {
                        return FileVisitResult.CONTINUE;
                    }

                    Files.delete(dir);
                    System.out.println("Deleted folder " + dir.toString());
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error deleting plex folder " + plexPath, ex);
        }

        try {
            Files.createDirectories(Paths.get(plexPath, StandaloneMovieWriter.PATH));
            Files.createDirectories(Paths.get(plexPath, SerieItemWriter.PATH));
            Files.createDirectories(Paths.get(plexPath, PlexMusicWriter.PATH));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Basic plex folder creation error", ex);
        }
    }
}