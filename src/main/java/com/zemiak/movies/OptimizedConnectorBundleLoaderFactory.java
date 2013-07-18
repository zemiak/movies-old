package com.zemiak.movies;

import java.util.HashSet;
import java.util.Set;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

public class OptimizedConnectorBundleLoaderFactory extends ConnectorBundleLoaderFactory {

    private Set<String> eagerConnectors = new HashSet<>();
    {
        eagerConnectors.add(com.vaadin.client.ui.ui.UIConnector.class.getName());
        eagerConnectors.add(com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationBarConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.csslayout.CssLayoutConnector.class.getName());
        eagerConnectors.add(com.vaadin.client.ui.button.ButtonConnector.class.getName());
        eagerConnectors.add(com.vaadin.addon.touchkit.gwt.client.vcom.VerticalComponentGroupConnector.class.getName());
        eagerConnectors.add(com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationManagerConnector.class.getName());
        eagerConnectors.add(com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationViewConnector.class.getName());
        eagerConnectors.add(com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationButtonConnector.class.getName());
        eagerConnectors.add(com.vaadin.addon.responsive.client.ResponsiveConnector.class.getName());
    }

    @Override
    protected LoadStyle getLoadStyle(JClassType connectorType) {
        if (eagerConnectors.contains(connectorType.getQualifiedBinaryName())) {
            return LoadStyle.EAGER;
        } else {
            return LoadStyle.EAGER;
        }
    }
}
