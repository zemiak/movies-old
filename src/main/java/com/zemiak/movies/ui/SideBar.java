package com.zemiak.movies.ui;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.zemiak.movies.MoviesTheme;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author peholmst
 */
@UIScoped
public class SideBar extends VerticalLayout {
    
    @Inject
    javax.enterprise.event.Event<SideBarActionRegistrationEvent> actionRegistrationEvent;
    private Map<String, SideBarSectionPanel> sectionPanels = new HashMap<>();

    @PostConstruct
    void init() {
        addStyleName("side-bar");
        final SideBarSectionPanel panel = new SideBarSectionPanel();
        panel.setSizeFull();
        addComponent(panel);
        setExpandRatio(panel, 1);

        setSizeFull();

        actionRegistrationEvent.fire(new SideBarActionRegistrationEvent() {
            @Override
            public void registerAction(String caption, Resource icon, String viewId) {
                panel.addAction(caption, icon, viewId);
            }
        });
    }

    private class SideBarSectionPanel extends Panel {

        private VerticalLayout layout;

        public SideBarSectionPanel() {
            addStyleName(MoviesTheme.PANEL_LIGHT);
            setSizeFull();
            layout = new VerticalLayout();
            layout.setWidth("100%");
            layout.addStyleName("section-content");
            setContent(layout);
        }

        public void addAction(String caption, Resource icon, final String viewId) {
            NativeButton button = new NativeButton(caption);
            button.setWidth("100%");
            button.setIcon(icon);
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    getUI().getNavigator().navigateTo(viewId);
                }
            });
            layout.addComponent(button);
        }
    }
}
