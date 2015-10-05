package com.zemiak.movies.batch.plex.music;

import com.zemiak.movies.batch.metadata.MetadataReader;
import com.zemiak.movies.batch.metadata.MovieMetadata;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class MusicWriter {
    static final String PATH = "Music";

    @Inject String musicPath;

    public void process(String plexFolder, String musicFileName) throws IOException {
        // Music/Artist_Name - Album_Name/Track_Number - Track_Name.ext
        MovieMetadata data = new MetadataReader(Paths.get(musicPath, musicFileName).toString()).get();
        String trackFileName = data.getNiceDisplayOrder() + " - " + data.getName() + ".m4a";
        Path folder = Paths.get(plexFolder, PATH, data.getArtist() + " - " + data.getAlbumName());
        Path linkName = Paths.get(folder.toString(), trackFileName);

        Files.createDirectories(folder);
        Path existing = Paths.get(musicFileName);
        Files.createSymbolicLink(linkName, existing);
    }
}
