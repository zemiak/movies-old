package com.zemiak.movies.ui.admin.view.about;

import com.zemiak.movies.ui.admin.SideBarActionRegistrationEvent;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class AboutRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
        event.registerAction("Settings", AboutView.ICON, AboutView.VIEW_ID);

   }
}
