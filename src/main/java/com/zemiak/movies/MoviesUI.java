package com.zemiak.movies;

import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.zemiak.movies.ui.NavManager;
import com.zemiak.movies.ui.NavToolbar;
import com.zemiak.movies.ui.ScreenSizeListener;
import javax.inject.Inject;

@SuppressWarnings("serial")
@Theme("books")
@Title("Books")
@Widgetset("com.zemiak.movies.AppWidgetSet")
@CDIUI
public class MoviesUI extends UI {
    @Inject
    private CDIViewProvider viewProvider;
    
    private Toolbar toolbar;

    @Override
    protected void init(VaadinRequest request) {
        initGUI();
    }

    private void initGUI() {
        getPage().addBrowserWindowResizeListener(new ScreenSizeListener());
        
        NavManager nav = new NavManager();
        setContent(nav);
        
        toolbar = new NavToolbar(nav);
        
        nav.setViewProvider(viewProvider);
        nav.navigateTo("about");
    }
    
    public Toolbar getToolbar() {
        return toolbar;
    }
}
