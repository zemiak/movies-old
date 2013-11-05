package com.zemiak.movies.ui.admin;

/**
 * @author petter@vaadin.com
 */
public interface Adaptable {

    boolean supports(Class<?> adapterClass);

    <T> T adapt(Class<T> adapterClass);
}
