package com.zemiak.movies.service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class BackupService {
    private static final Logger LOG = Logger.getLogger(BackupService.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject String backupPath;

    public void backup() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Path filePath = Paths.get(backupPath, formatter.format(new Date()) + "_movies.plain");

        Query query = em.createNativeQuery("SCRIPT TO '" + filePath + "'");
        query.getResultList();

        LOG.log(Level.INFO, "Backed up the database to {0}", filePath);
    }

    public void removeOldBackups() {
        Path directory = Paths.get(backupPath);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (olderThanOneMonth(attrs)) {
                        Files.delete(file);
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error deleting old backup files", ex);
        }
    }

    private boolean olderThanOneMonth(BasicFileAttributes attrs) {
        Instant lastModified = attrs.lastModifiedTime().toInstant();
        Instant lastMonth = Instant.now().minus(Duration.ofDays(31));

        return lastModified.isBefore(lastMonth);
    }
}
