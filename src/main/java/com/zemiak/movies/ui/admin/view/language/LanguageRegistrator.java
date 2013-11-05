package com.zemiak.movies.ui.admin.view.language;

import com.zemiak.movies.ui.admin.SideBarActionRegistrationEvent;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class LanguageRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
        event.registerAction("Language", LanguageListView.ICON, LanguageListView.VIEW_ID);

   }
}
