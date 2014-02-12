package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.domain.Movie;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vasko
 */
public class MovieMetadata {
    private static final Logger LOG = Logger.getLogger(MovieMetadata.class.getName());

    private String genre;
    private String name;
    private String comments;

    public MovieMetadata() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "MovieMetadata{" + "genre=" + genre + ", name=" + name + ", comments=" + comments + '}';
    }

    public boolean commentsShouldBeUpdated(Movie movie) {
        boolean commentsAreEmpty = (comments == null
                || comments.trim().isEmpty()
                || "''".equals(comments));

        if (commentsAreEmpty && !movie.isDescriptionEmpty()) {
            LOG.log(Level.INFO, "{0}: metadata comments empty, movie comments not", movie.getFileName());
            return true;
        }

        if (commentsAreEmpty && movie.isDescriptionEmpty() && !movie.isUrlEmpty()) {
            LOG.log(Level.INFO, "{0}: metadata and movie comments empty, comments URL not", movie.getFileName());
            return true;
        }

        if (!commentsAreEmpty && !movie.isDescriptionEmpty() && !comments.equals(movie.getDescription())) {
            LOG.log(Level.INFO, "{0}: metadata and comments not empty, but not equal", movie.getFileName());
            return true;
        }

        return false;
    }

    public boolean commentsShouldBeUpdatedQuiet(Movie movie) {
        boolean commentsAreEmpty = (comments == null
                || comments.trim().isEmpty()
                || "''".equals(comments));

        if (commentsAreEmpty && !movie.isDescriptionEmpty()) {
            return true;
        }

        if (commentsAreEmpty && movie.isDescriptionEmpty() && !movie.isUrlEmpty()) {
            return true;
        }

        return !commentsAreEmpty && !movie.isDescriptionEmpty() && !comments.equals(movie.getDescription());
    }
}
