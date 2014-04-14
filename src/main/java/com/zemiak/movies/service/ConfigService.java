
package com.zemiak.movies.service;

import javax.annotation.Resource;
import javax.inject.Singleton;

/**
 *
 * @author vasko
 */
@Singleton
public class ConfigService {
    @Resource(lookup = "com.zemiak.movies.backendUrl")
    private String backendUrl;

    @Resource(lookup = "com.zemiak.movies.ffmpeg")
    private String ffmpeg;

    @Resource(lookup = "com.zemiak.movies.imgPath")
    private String imgPath;

    @Resource(lookup = "com.zemiak.movies.mailFrom")
    private String mailFrom;

    @Resource(lookup = "com.zemiak.movies.mailSubject")
    private String mailSubject;

    @Resource(lookup = "com.zemiak.movies.mailTo")
    private String mailTo;

    @Resource(lookup = "com.zemiak.movies.mp4info")
    private String mp4info;

    @Resource(lookup = "com.zemiak.movies.mp4tags")
    private String mp4tags;

    @Resource(lookup = "com.zemiak.movies.path")
    private String path;

    public String getBackendUrl() {
        return backendUrl;
    }

    public String getFfmpeg() {
        return ffmpeg;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getMp4info() {
        return mp4info;
    }

    public String getMp4tags() {
        return mp4tags;
    }

    public String getPath() {
        return path;
    }
}
