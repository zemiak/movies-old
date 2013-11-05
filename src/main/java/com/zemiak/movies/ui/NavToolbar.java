package com.zemiak.movies.ui;

import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.zemiak.movies.ui.admin.AdminLayout;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class NavToolbar extends Toolbar {
    NavManager nav;
    
    @Inject
    Instance<AdminLayout> adminLayout;

    public NavToolbar(NavManager nav) {
        super();

        this.nav = nav;

        genresButton();
        searchButton();
        adminButton();
        aboutButton();
    }

    private void genresButton() {
        Button button = new Button();
        button.setIcon(new ThemeResource("icons/tag.png"));
        button.setCaption("Genres");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav.navigateTo("genres");
            }
        });
        addComponent(button);
    }

    private void searchButton() {
        Button button = new Button();
        button.setIcon(new ThemeResource("icons/search.png"));
        button.setCaption("Search");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                nav.navigateTo("search");
            }
        });
        addComponent(button);
    }

    private void adminButton() {
        Button button = new Button();
        button.setIcon(new ThemeResource("icons/settings.png"));
        button.setCaption("Admin");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().setContent(adminLayout.get());
            }
        });
        addComponent(button);
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
