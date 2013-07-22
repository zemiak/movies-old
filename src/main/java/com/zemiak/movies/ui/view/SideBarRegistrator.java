package com.zemiak.movies.ui.view;

import com.zemiak.movies.ui.SideBarActionRegistrationEvent;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class SideBarRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
        event.registerAction("About", AboutView.ICON, AboutView.VIEW_ID);
    }
}
