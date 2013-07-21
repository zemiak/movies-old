package com.zemiak.movies.ui.view;

import com.zemiak.movies.ui.SideBarActionRegistrationEvent;
import com.zemiak.movies.ui.view.admin.language.LanguageListView;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class SideBarRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
        event.registerAction("Language", LanguageListView.ICON, LanguageListView.VIEW_ID);
        event.registerAction("About", AboutView.ICON, AboutView.VIEW_ID);
    }
}
