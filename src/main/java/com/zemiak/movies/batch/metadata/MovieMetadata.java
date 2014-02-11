package com.zemiak.movies.batch.metadata;

import com.zemiak.movies.domain.Movie;

/**
 *
 * @author vasko
 */
public class MovieMetadata {
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
            return true;
        }

        if (commentsAreEmpty && movie.isDescriptionEmpty() && !movie.isUrlEmpty()) {
            return true;
        }

        return !commentsAreEmpty && !movie.isDescriptionEmpty() && !comments.equals(movie.getDescription());
    }
}
