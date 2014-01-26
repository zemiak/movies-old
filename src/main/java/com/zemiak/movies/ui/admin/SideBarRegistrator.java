package com.zemiak.movies.ui.admin;


import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

/**
 *
 * @author peholmst
 */
@Dependent
public class SideBarRegistrator {
    public void registerActions(@Observes SideBarActionRegistrationEvent event) {
    }
}
