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
    private Movie movie;

    public MovieMetadata(final Movie movie) {
        this.movie = movie;
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

    private boolean commentsShouldBeUpdated0(final boolean debug) {
        boolean commentsAreEmpty = (comments == null
                || comments.trim().isEmpty()
                || "''".equals(comments));

        if (commentsAreEmpty && !movie.isDescriptionEmpty()) {
            if (debug) {
                LOG.log(Level.INFO, "{0}: metadata comments empty, movie comments not", movie.getFileName());
            }
            
            return true;
        }

        if (commentsAreEmpty && movie.isDescriptionEmpty() && !movie.isUrlEmpty()) {
            if (debug) {
                LOG.log(Level.INFO, "{0}: metadata and movie comments empty, comments URL not", movie.getFileName());
            }
            
            return true;
        }

        if (!commentsAreEmpty && !movie.isDescriptionEmpty() && !comments.equals(movie.getDescription())) {
            if (debug) {
                LOG.log(Level.INFO, "{0}: metadata and comments not empty, but not equal", movie.getFileName());
            }
            
            return true;
        }

        return false;
    }
    
    public boolean commentsShouldBeUpdated() {
        return commentsShouldBeUpdated0(true);
    }

    public boolean commentsShouldBeUpdatedQuiet() {
        return commentsShouldBeUpdated0(false);
    }
    
    public boolean isNameEqual() {
        if (name == null && movie.getName() != null) {
            return false;
        }
        
        return name == null || name.equals(movie.getName());
    }
    
    public boolean isGenreEqual() {
        if (genre == null) {
            return false;
        }
        
        return genre.equals(movie.composeGenreName());
    }
    
    private boolean isMetadataEqual0(final boolean debug) {
        boolean ret = true;
        
        if (! isNameEqual()) {
            ret = false;
            
            if (debug) {
                LOG.log(Level.INFO, "{0}: isMetadataEqual: Name is not equal", movie.getFileName());
            }
        }
        
        if (! isGenreEqual()) {
            ret = false;
            
            if (debug) {
                LOG.log(Level.INFO, "{0}: isMetadataEqual: Genre is not equal", movie.getFileName());
            }
        }
        
        if (commentsShouldBeUpdated0(debug)) {
            ret = false;
        }
        
        return ret;
    }
    
    public boolean isMetadataEqual() {
        return isMetadataEqual0(true);
    }

    public Movie getMovie() {
        return movie;
    }
}
