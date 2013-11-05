package com.zemiak.movies.ui.admin;

import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.zemiak.movies.MoviesTheme;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * @author petter@vaadin.com
 */
@Dependent
public class AdminLayout extends VerticalLayout {

    @Inject
    SideBar sideBar;

    private Panel moduleContent;

    @Inject
    Header header;

    @Inject
    CDIViewProvider viewProvider;

    @Inject
    ErrorView errorView;
    
    public void AdminLayout() {
    }

    @PostConstruct
    public void init() {
        addStyleName("main-ui-content");
        setSizeFull();
        addComponent(header);

        final HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.setSizeFull();
        split.addStyleName(MoviesTheme.SPLITPANEL_SMALL);
        split.setSplitPosition(150f, Unit.PIXELS);
        addComponent(split);
        setExpandRatio(split, 1);

        split.setFirstComponent(sideBar);

        moduleContent = new Panel();
        moduleContent.setSizeFull();
        moduleContent.addStyleName(MoviesTheme.PANEL_LIGHT);
        split.setSecondComponent(moduleContent);

        final Navigator navigator = new Navigator(UI.getCurrent(), moduleContent);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(errorView);
    }
}
