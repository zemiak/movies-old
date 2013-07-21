package com.zemiak.movies.ui;

import com.vaadin.cdi.UIScoped;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import javax.annotation.PostConstruct;

/**
 *
 * @author peholmst
 */
@UIScoped
public class Header extends HorizontalLayout implements ViewChangeListener {

    private Label title;

    private MenuBar menuBar;

    private HorizontalLayout menuBarPlaceholder;

    @PostConstruct
    void init() {
        addStyleName("header");
        setWidth("100%");

        title = new Label("Zemiak Movies");
        title.addStyleName("title");
        title.setSizeUndefined();
        addComponent(title);

        menuBarPlaceholder = new HorizontalLayout();
        menuBarPlaceholder.setWidth("100%");
        addComponent(menuBarPlaceholder);
        setExpandRatio(menuBarPlaceholder, 1);
    }

    @Override
    public void attach() {
        super.attach();
        getUI().getNavigator().addViewChangeListener(this);
    }

    @Override
    public void detach() {
        getUI().getNavigator().removeViewChangeListener(this);
        super.detach();
    }

    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        if (event.getNewView() instanceof Adaptable) {
            Adaptable adaptableView = (Adaptable) event.getNewView();
            if (adaptableView.supports(MenuBar.class)) {
                setMenuBar(adaptableView.adapt(MenuBar.class));
                return;
            }
        }
        setMenuBar(null);
    }

    private void setMenuBar(MenuBar menuBar) {
        if (this.menuBar != null) {
            menuBarPlaceholder.removeComponent(this.menuBar);
        }
        this.menuBar = menuBar;
        if (this.menuBar != null) {
            menuBar.setStyleName("menu");
            menuBar.setWidth("100%");
            menuBarPlaceholder.addComponent(menuBar);
        }
    }

    private void changePassword() {
        Notification.show("Not implemented in this demo");
    }

    private void changeAccountInfo() {
        Notification.show("Not implemented in this demo");
    }

    private void logout() {
        String currentLocation = getUI().getPage().getLocation().toString();
        String newLocation;
        
        if (currentLocation.contains("#!")) {
            newLocation = currentLocation.substring(0, currentLocation.indexOf("#!"));
        } else {
            newLocation = currentLocation;
        }
        
        getUI().getSession().getSession().invalidate();
        getUI().getPage().setLocation(newLocation);
    }
}
