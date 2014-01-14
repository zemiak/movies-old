package com.zemiak.batch.metadata.description;

/**
 *
 * @author vasko
 */
public interface IDescriptionReader {
    public boolean acceptsUrl(final String url);
    public String getDescription(final String url);
}
