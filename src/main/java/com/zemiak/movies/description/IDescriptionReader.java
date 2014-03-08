package com.zemiak.movies.description;

import com.zemiak.movies.domain.Movie;
import java.util.Map;

/**
 *
 * @author vasko
 */
public interface IDescriptionReader {
    public boolean accepts(final Movie movie);
    public String getDescription(final Movie movie);
    public Map<String, String> getUrlCandidates(final String movieName);
    public String getReaderName();
}
