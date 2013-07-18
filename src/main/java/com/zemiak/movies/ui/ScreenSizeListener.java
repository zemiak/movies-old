package com.zemiak.movies.ui;

import com.vaadin.server.Page;

/**
 *
 * @author vasko
 */
public class ScreenSizeListener implements Page.BrowserWindowResizeListener {

    @Override
    public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
        if (event.getWidth() > event.getHeight()) {
            System.out.println("Landscape");
        } else {
            System.out.println("Portrait");
        }
    }
    
}
