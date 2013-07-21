package com.zemiak.movies.ui;

import com.vaadin.server.Resource;

/**
 *
 * @author peholmst
 */
public interface SideBarActionRegistrationEvent {

    void registerAction(String caption, Resource icon, String viewId);
    
}
