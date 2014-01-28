package com.zemiak.movies.batch.metadata.description;

/**
 *
 * @author vasko
 */
public class MashDescription {
    private String title;
    private String description;

    public MashDescription(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public MashDescription() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
