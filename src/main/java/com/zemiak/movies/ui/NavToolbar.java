package com.zemiak.movies.ui;

import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.zemiak.movies.ui.admin.AdminLayout;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Named
public class NavToolbar extends Toolbar {
    NavManager nav;
    
    public NavToolbar() {
    }

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
        button.setIcon(new ThemeResource("icons/video.png"));
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
                UI.getCurrent().setContent(getAdminLayout());
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
    
    private Component getAdminLayout() {
        BeanManager beanManager;
        
        try {
            beanManager = InitialContext.doLookup("java:comp/BeanManager");
        } catch (NamingException ex) {
            Logger.getLogger(NavToolbar.class.getName()).log(Level.SEVERE, "Cannot find AdminLayout", ex);
            return null;
        }
        
        Set<Bean<?>> beans = beanManager.getBeans(AdminLayout.class);
        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext<?> cc = beanManager.createCreationalContext(bean);

        AdminLayout adminLayout = (AdminLayout) beanManager.getReference(bean, AdminLayout.class, cc);
        
        return adminLayout;
    }
}
