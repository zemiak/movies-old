package com.zemiak.movies.ui.view.components;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;

/**
 *
 * @author vasko
 */
public class ImageButton extends NavigationButton {
    public ImageButton(final String caption, final Resource icon) {
        super(caption);
        setSizeUndefined();
        setIcon(icon);
        addStyleName("image-button");
    }
}
