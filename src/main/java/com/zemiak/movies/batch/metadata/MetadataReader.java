package com.zemiak.movies.batch.metadata;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.MetaBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.UnknownBox;
import com.coremedia.iso.boxes.UserDataBox;
import com.coremedia.iso.boxes.apple.AppleItemListBox;
import com.zemiak.movies.batch.service.log.BatchLogger;
import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.logging.Level;

/**
 *
 * @author vasko
 */
public class MetadataReader {
    private static final BatchLogger LOG = BatchLogger.getLogger(MetadataReader.class.getName());

    private final MovieMetadata metaData;
    private final String fileName;

    public MetadataReader(final String name, final Movie movie) {
        metaData = new MovieMetadata(movie);
        fileName = name;

        process();
    }

    private void process() {
        IsoFile isoFile;

        try {
            isoFile = new IsoFile(fileName);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot open file", ex);
            return;
        }

        MovieBox moov = isoFile.getMovieBox();
        for (Box b : moov.getBoxes()) {
            if ("udta".equals(b.getType())) {
                processUserData((UserDataBox) b);
            }
        }
        try {
            isoFile.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot close file", ex);
        }
    }

    private void processUserData(final UserDataBox box) {
        for (Box subBox : box.getBoxes()) {
            if ("meta".equals(subBox.getType())) {
                processMetaBox((MetaBox) subBox);
            }
        }
    }

    private void processMetaBox(final MetaBox box) {
        for (Box subBox : box.getBoxes()) {
            if ("ilst".equals(subBox.getType())) {
                processAppleBox((AppleItemListBox) subBox);
            }
        }
    }

    private void processAppleBox(final AppleItemListBox box) {
        for (Box subBox : box.getBoxes()) {
            String data = unknownBoxToString((UnknownBox) subBox);

            switch (box.getType()) {
                case "©too":
                    metaData.setEncoder(data);
                    break;

                case "©nam":
                    metaData.setName(data);
                    break;

                case "©ART":
                    metaData.setArtist(data);
                    break;

                case "aART":
                    metaData.setAlbumArtist(data);
                    break;

                case "©wrt":
                    metaData.setComposer(data);
                    break;

                case "©alb":
                    metaData.setAlbumName(data);
                    break;

                case "©grp":
                    metaData.setGrouping(data);
                    break;

                case "©gen":
                    metaData.setGenre(data);
                    break;

                case "©day":
                    metaData.setYear(data);
                    break;

                case "©cmt":
                    metaData.setComments(data);
                    break;
            }
        }
    }

    public String unknownBoxToString(final UnknownBox box) {
        ByteBuffer buffer = box.getData();
        String data;
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();

        try {
            int old_position = buffer.position();
            buffer.rewind();
            data = decoder.decode(buffer).toString();
            // reset buffer's position to its original so it is not altered:
            buffer.position(old_position);
        } catch (CharacterCodingException e) {
            LOG.log(Level.SEVERE, "Cannot decode string data for box " + box.getType(), e);
            return "";
        }

        if (data.contains("data")) {
            data = data.substring(data.indexOf("data") + 4).trim();
        }

        return data;
    }

    public MovieMetadata get() {
        return metaData;
    }
}
