package com.zemiak.movies.ui.view;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.zemiak.movies.ui.NavManager;
import com.zemiak.movies.ui.NavToolbar;

abstract public class ViewAbstract extends NavigationView implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        onBecomingVisible();
    }
    
    public NavManager getNavManager() {
        return (NavManager) getNavigationManager();
    }
    
    @Override
    public void attach() {
        super.attach();
        
        Toolbar toolbar = new NavToolbar(getNavManager());
        this.setToolbar(toolbar);
    }
}
