package com.zemiak.movies.ui.admin.view.serie;

import com.zemiak.movies.ui.admin.SideBarActionRegistrationEvent;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class SerieRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
        event.registerAction("Serie", SerieListView.ICON, SerieListView.VIEW_ID);

   }
}
