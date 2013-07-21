package com.zemiak.movies;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.cdi.CDIUI;
import com.zemiak.movies.ui.MoviesLayout;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@SuppressWarnings("serial")
@Theme(MoviesTheme.THEMENAME)
@Title("Movies")
@Widgetset("com.zemiak.movies.AppWidgetSet")
@CDIUI
public class MoviesUI extends UI {
    @Inject
    Instance<MoviesLayout> layout;
    
    @Override
    protected void init(VaadinRequest request) {
        setContent(layout.get());
    }
}
