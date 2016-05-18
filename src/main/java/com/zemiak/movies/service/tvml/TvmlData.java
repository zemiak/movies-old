package com.zemiak.movies.service.tvml;

import java.util.ArrayList;
import java.util.List;

public class TvmlData {
    private List<FolderData> folders;
    private List<MovieData> movies;

    public TvmlData() {
        folders = new ArrayList<>();
        movies = new ArrayList<>();
    }

    public List<FolderData> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderData> folders) {
        this.folders = folders;
    }

    public List<MovieData> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieData> movies) {
        this.movies = movies;
    }
}
