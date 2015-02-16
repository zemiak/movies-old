package com.zemiak.movies.service.description;

import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.UrlDTO;
import java.util.List;

/**
 *
 * @author vasko
 */
public interface IDescriptionReader {
    public boolean accepts(final Movie movie);
    public String getDescription(final Movie movie);
    public List<UrlDTO> getUrlCandidates(final String movieName);
    public String getReaderName();
}
