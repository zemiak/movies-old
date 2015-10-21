package com.zemiak.movies.batch.plex;

import com.zemiak.movies.batch.movies.MetadataReader;
import com.zemiak.movies.batch.movies.MovieMetadata;
import com.zemiak.movies.batch.service.BatchLogger;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1_1;
import org.farng.mp3.id3.ID3v2_2;

@Dependent
public class PlexMusicWriter {
    private static final BatchLogger LOG = BatchLogger.getLogger(PlexMusicWriter.class.getName());
    static final String PATH = "Music";

    @Inject String plexPath;

    public void process(final List<String> list) {
        list.stream().filter(obj -> null != obj).forEach(musicFileName -> {
            try {
                process(musicFileName);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Cannot create a song link " + musicFileName + ": " + ex.getMessage(), ex);
            }
        });
    }

    private void process(String musicFileName) throws IOException {
        // Music/Artist_Name - Album_Name/Track_Number - Track_Name.ext
        String ext = PrepareMusicFileList.getFileExtension(musicFileName);

        MovieMetadata data = getSongMetadata(musicFileName, ext);
        if (null == data) {
            LOG.log(Level.SEVERE, "No metadata for the song {0}", musicFileName);
            return;
        }

        String trackFileName = null != data.getNiceDisplayOrder()
                ? data.getNiceDisplayOrder() + " - " + data.getName() + "." + ext
                : musicFileName.substring(musicFileName.lastIndexOf("/") + 1);
        Path folder = Paths.get(plexPath, PATH, StandaloneMovieWriter.deAccent(data.getArtist() + " - " + data.getAlbumName()));
        Path linkName = Paths.get(folder.toString(), StandaloneMovieWriter.deAccent(trackFileName));

        Files.createDirectories(folder);
        Path existing = Paths.get(musicFileName);
        Files.createSymbolicLink(linkName, existing);

        LOG.log(Level.FINEST, "Created music link {0} -> {1}", new Object[]{linkName.toString(), existing.toString()});
    }

    private MovieMetadata getSongMetadata(String fileName, String ext) {
        MovieMetadata data;

        try {
            data = "mp3".equals(ext) ? getSongMetadataMp3(fileName)
                    : new MetadataReader(fileName).get();
        } catch (java.lang.UnsupportedOperationException ex) {
            // tag info not available
            data = null;
        }

        return (null == data || null == data.getName() || "".equals(data.getName().trim())
                || null == data.getAlbumName() || "".equals(data.getAlbumName().trim())
                || null == data.getArtist() || "".equals(data.getArtist().trim()))
            ? null : data;
    }

    private MovieMetadata getSongMetadataMp3(String fileName) {
        MovieMetadata data;

        try {
            data = getSongMetadataMp3v2(fileName);
        } catch (IllegalStateException ex) {
            data = null;
        }

        if (null == data || null == data.getName()) {
            try {
                data = getSongMetadataMp3v1(fileName);
            } catch (IllegalStateException ex) {
                return null;
            }
        }

        return data;
    }

    private MovieMetadata getSongMetadataMp3v1(String fileName) {
        MovieMetadata data = new MovieMetadata();
        File file = new File(fileName);
        ID3v2_2 tag;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            tag = new ID3v2_2(raf);
        } catch (IOException | TagException ex) {
            throw new IllegalStateException(ex);
        }

        data.setNiceDisplayOrder(tag.getTrackNumberOnAlbum());
        data.setArtist(null == tag.getAuthorComposer() ? tag.getLeadArtist() : tag.getAuthorComposer());
        data.setAlbumName(tag.getAlbumTitle());
        data.setName(tag.getSongTitle());

        return data;
    }

    private MovieMetadata getSongMetadataMp3v2(String fileName) {
        MovieMetadata data = new MovieMetadata();
        File file = new File(fileName);
        ID3v1_1 tag;

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            tag = new ID3v1_1(raf);
        } catch (IOException | TagException ex) {
            throw new IllegalStateException(ex);
        }

        data.setNiceDisplayOrder(tag.getTrackNumberOnAlbum());
        data.setArtist(tag.getAuthorComposer());
        data.setAlbumName(tag.getAlbumTitle());
        data.setName(tag.getSongTitle());

        return data;
    }
}
