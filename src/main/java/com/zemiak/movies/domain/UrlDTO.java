package com.zemiak.movies.domain;

/**
 *
 * @author vasko
 */
public class UrlDTO {
    private String url;
    private String desc;

    public UrlDTO() {
    }

    public UrlDTO(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
