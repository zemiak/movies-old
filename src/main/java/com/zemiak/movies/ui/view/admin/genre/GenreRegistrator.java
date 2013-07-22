package com.zemiak.movies.ui.view.admin.genre;

import com.zemiak.movies.ui.SideBarActionRegistrationEvent;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class GenreRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
        event.registerAction("Genre", GenreListView.ICON, GenreListView.VIEW_ID);

   }
}
