package com.zemiak.movies.batch.plex;

import com.zemiak.movies.domain.Movie;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class StandaloneMovieWriter {
    static final String PATH = "Movies";
    private static final Logger LOG = Logger.getLogger(StandaloneMovieWriter.class.getName());

    @Inject String path;
    @Inject String plexPath;

    public void process(Movie movie) throws IOException {
        String movieName = (null == movie.getOriginalName() || "".equals(movie.getOriginalName().trim()))
                ? movie.getName() : movie.getOriginalName();
        Path linkName = Paths.get(plexPath, PATH, deAccent(movieName) + ".m4v");

        if (null == movieName || "".equals(movieName)) {
            LOG.log(Level.SEVERE, "Movie {0} has no name", movie.getFileName());
            return;
        }

        Integer i = 2;
        while (Files.exists(linkName)) {
            linkName = Paths.get(plexPath, PATH, movieName +  "-" + String.valueOf(i++) + ".m4v");

            if (i > 100) {
                throw new IllegalStateException("Cannot find a suitable name for " + linkName.toString());
            }
        }

        Path existing = Paths.get(path, movie.getFileName());
        Files.createSymbolicLink(linkName, existing);

        LOG.log(Level.FINEST, "Created movie link {0} -> {1}", new Object[]{linkName.toString(), existing.toString()});
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String asciiOnly = pattern.matcher(nfdNormalizedString).replaceAll("");
        return asciiOnly.replaceAll("[^\\x00-\\x7F]", "");
    }
}