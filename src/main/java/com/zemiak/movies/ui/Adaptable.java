package com.zemiak.movies.ui;

/**
 * @author petter@vaadin.com
 */
public interface Adaptable {

    boolean supports(Class<?> adapterClass);

    <T> T adapt(Class<T> adapterClass);
}
