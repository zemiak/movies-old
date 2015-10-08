package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.movies.MetadataReader;
import com.zemiak.movies.batch.movies.MovieMetadata;
import com.zemiak.movies.batch.service.BatchLogger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class PlexMusicWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(PlexMusicWriter.class.getName());
    static final String PATH = "Music";

    @Inject String musicPath;
    @Inject String plexPath;

    public void process(final List<String> list) {
        list.stream().filter(obj -> null != obj).forEach(musicFileName -> {
            try {
                process(musicFileName);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot create a song link " + musicFileName, ex);
            }
        });
    }

    public void process(String musicFileName) throws IOException {
        // Music/Artist_Name - Album_Name/Track_Number - Track_Name.ext
        MovieMetadata data = new MetadataReader(Paths.get(musicPath, musicFileName).toString()).get();
        String trackFileName = data.getNiceDisplayOrder() + " - " + data.getName() + ".m4a";
        Path folder = Paths.get(plexPath, PATH, data.getArtist() + " - " + data.getAlbumName());
        Path linkName = Paths.get(folder.toString(), trackFileName);

        Files.createDirectories(folder);
        Path existing = Paths.get(musicFileName);
        Files.createSymbolicLink(linkName, existing);
        
        LOG.log(Level.INFO, "Created music link {0} -> {1}", new Object[]{linkName.toString(), existing.toString()});
    }
}
