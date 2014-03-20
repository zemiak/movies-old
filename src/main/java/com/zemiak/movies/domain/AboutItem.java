package com.zemiak.movies.domain;

/**
 *
 * @author vasko
 */
public class AboutItem {
    private String title;
    private String value;
    private Integer id;

    public AboutItem() {
    }

    public AboutItem(Integer id, String title, String value) {
        this.id = id;
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
