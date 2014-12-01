package com.zemiak.movies.service.configuration;

import javax.inject.Singleton;

/**
 *
 * @author vasko
 */
@Singleton
public class Configuration {
    private String backendUrl, ffmpeg, imgPath, mailFrom, mailSubject,
            mp4info, mp4tags, path, mailTo, imgServer, playServer;
    private boolean developmentSystem;

    public Configuration() {
    }

    public String getBackendUrl() {
        return backendUrl;
    }

    public void setBackendUrl(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public String getFfmpeg() {
        return ffmpeg;
    }

    public void setFfmpeg(String ffmpeg) {
        this.ffmpeg = ffmpeg;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMp4info() {
        return mp4info;
    }

    public void setMp4info(String mp4info) {
        this.mp4info = mp4info;
    }

    public String getMp4tags() {
        return mp4tags;
    }

    public void setMp4tags(String mp4tags) {
        this.mp4tags = mp4tags;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public boolean isDevelopmentSystem() {
        return developmentSystem;
    }

    public void setDevelopmentSystem(boolean developmentSystem) {
        this.developmentSystem = developmentSystem;
    }

    public String getImgServer() {
        return imgServer;
    }

    public void setImgServer(String imgServer) {
        this.imgServer = imgServer;
    }

    public String getPlayServer() {
        return playServer;
    }

    public void setPlayServer(String playServer) {
        this.playServer = playServer;
    }
}
