package com.zemiak.movies.ui.admin.view.movie;

import com.zemiak.movies.ui.admin.view.movie.list.MovieListView;
import com.zemiak.movies.ui.admin.SideBarActionRegistrationEvent;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class MovieRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
        event.registerAction("Movie", MovieListView.ICON, MovieListView.VIEW_ID);

   }
}