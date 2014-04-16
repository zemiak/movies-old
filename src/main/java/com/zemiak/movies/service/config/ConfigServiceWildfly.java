package com.zemiak.movies.service.config;

import javax.annotation.Resource;

/**
 *
 * @author vasko
 */
public class ConfigServiceWildfly implements IConfigService {
    @Resource(name = "java:jboss/com.zemiak.movies.backendUrl")
    private String backendUrl;

    @Resource(name = "java:jboss/com.zemiak.movies.ffmpeg")
    private String ffmpeg;

    @Resource(name = "java:jboss/com.zemiak.movies.imgPath")
    private String imgPath;

    @Resource(name = "java:jboss/com.zemiak.movies.mailFrom")
    private String mailFrom;

    @Resource(name = "java:jboss/com.zemiak.movies.mailSubject")
    private String mailSubject;

    @Resource(name = "java:jboss/com.zemiak.movies.mailTo")
    private String mailTo;

    @Resource(name = "java:jboss/com.zemiak.movies.mp4info")
    private String mp4info;

    @Resource(name = "java:jboss/com.zemiak.movies.mp4tags")
    private String mp4tags;

    @Resource(name = "java:jboss/com.zemiak.movies.path")
    private String path;

    @Override
    public String getBackendUrl() {
        return backendUrl;
    }

    @Override
    public String getFfmpeg() {
        return ffmpeg;
    }

    @Override
    public String getImgPath() {
        return imgPath;
    }

    @Override
    public String getMailFrom() {
        return mailFrom;
    }

    @Override
    public String getMailSubject() {
        return mailSubject;
    }

    @Override
    public String getMailTo() {
        return mailTo;
    }

    @Override
    public String getMp4info() {
        return mp4info;
    }

    @Override
    public String getMp4tags() {
        return mp4tags;
    }

    @Override
    public String getPath() {
        return path;
    }
}
