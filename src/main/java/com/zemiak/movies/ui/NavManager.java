package com.zemiak.movies.ui;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.View;

public class NavManager extends NavigationManager {
    private CDIViewProvider viewProvider;
    
    public NavManager() {
        super();
    }

    public CDIViewProvider getViewProvider() {
        return viewProvider;
    }

    public void setViewProvider(CDIViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }
    
    public View getView(String name) {
        return this.viewProvider.getView(name);
    }
    
    public void navigateTo(String name) {
        this.navigateTo((NavigationView) getView(name));
    }
}
