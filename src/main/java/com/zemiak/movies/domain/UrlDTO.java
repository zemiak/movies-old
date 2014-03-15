package com.zemiak.movies.domain;

/**
 *
 * @author vasko
 */
public class UrlDTO {
    private String url;
    private String description;
    private String dbName;
    private String movieName;

    public UrlDTO() {
    }

    public UrlDTO(final String url, final String dbName, final String movieName, final String description) {
        this.url = url;
        this.dbName = dbName;
        this.movieName = movieName;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(final String dbName) {
        this.dbName = dbName;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}
