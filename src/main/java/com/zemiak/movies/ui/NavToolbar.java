package com.zemiak.movies.ui;

import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;

public class NavToolbar extends Toolbar {
    NavManager nav;

    public NavToolbar(NavManager nav) {
        super();

        this.nav = nav;

        aboutButton();
    }

    private void aboutButton() {
        Button button = new Button();
        button.setIcon(new ThemeResource("icons/stats.png"));
        button.setCaption("About");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav.navigateTo("about");
            }
        });
        addComponent(button);
    }
}
