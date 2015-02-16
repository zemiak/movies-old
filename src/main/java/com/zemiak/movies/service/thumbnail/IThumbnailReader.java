package com.zemiak.movies.service.thumbnail;

import com.zemiak.movies.domain.Movie;

public interface IThumbnailReader {
    public boolean accepts(final Movie movie);
    public void process(final Movie movie);
    public String getReaderName();
    public void setImageFileName(final String imageFileName);
}
