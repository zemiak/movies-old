package com.zemiak.movies;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.TouchKitSettings;
import com.vaadin.addon.touchkit.settings.ViewPortSettings;
import com.vaadin.cdi.CDIUIProvider;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import javax.inject.Inject;
import javax.servlet.ServletException;

public class MoviesServlet extends TouchKitServlet {
    @Inject
    CDIUIProvider uiProvider;
    
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        TouchKitSettings s = getTouchKitSettings();
        
        initIcons(s);
        initFixedWidth(s);
        iniAppleWebApp(s);
        
        initCDI();
    }

    private void initCDI() {
        getService().addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {
                event.getSession().addUIProvider(uiProvider);
            }
        });
    }

    private void initIcons(TouchKitSettings s) {
        s.getApplicationIcons().addApplicationIcon(57, 57, "./VAADIN/themes/books/icons/icon-57.jpg", false);
        s.getApplicationIcons().addApplicationIcon(72, 72, "./VAADIN/themes/books/icons/icon-72.jpg", false);
        s.getApplicationIcons().addApplicationIcon(144, 144, "./VAADIN/themes/books/icons/icon-144.jpg", false);
        s.getApplicationIcons().addApplicationIcon(512, 512, "./VAADIN/themes/books/icons/icon-512.jpg", false);
        s.getApplicationIcons().addApplicationIcon(1024, 1024, "./VAADIN/themes/books/icons/icon-1024.jpg", false);
    }

    private void initFixedWidth(TouchKitSettings s) {
        ViewPortSettings vp = s.getViewPortSettings();
        vp.setViewPortUserScalable(false);
    }

    private void iniAppleWebApp(TouchKitSettings s) {
        s.getWebAppSettings().setWebAppCapable(true);
    }
}
