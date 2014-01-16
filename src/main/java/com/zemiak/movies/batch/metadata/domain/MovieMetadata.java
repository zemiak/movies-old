package com.zemiak.movies.batch.metadata.domain;

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
}
