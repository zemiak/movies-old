package com.zemiak.movies.ui.view;

import com.vaadin.cdi.CDIView;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.zemiak.movies.domain.CacheClearEvent;
import javax.inject.Inject;

@CDIView("about")
class About extends ViewAbstract {
    CssLayout content = null;
    public final String VERSION = "1.0";
    boolean initialized = false;
    
    @Inject 
    private javax.enterprise.event.Event<CacheClearEvent> clearEvent;

    public About() {
    }
    
    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        this.setCaption("About");
        
        if (initialized) {
            return;
        }
        
        refresh();
        initialized = true;
    }
    
    private void refresh() {
        content = new CssLayout();
        setContent(content);
        
        Table table = new Table("Statistics");
        table.addContainerProperty(1, String.class, null);
        table.addContainerProperty(2, Label.class, null);
        table.setColumnAlignment(2, Table.Align.RIGHT);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        table.setWidth("100%");
        table.setHeight("10em");
        
        table.addItem(new Object[]{"Version", 
            new Label(getBold(VERSION), ContentMode.HTML)}, 1);
        content.addComponent(table);
        
        NativeButton clearCacheButton = new NativeButton("Clear Cache", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clearEvent.fire(new CacheClearEvent());
                Notification.show("Data Cache Cleared", Notification.Type.HUMANIZED_MESSAGE);
            }
        });
        clearCacheButton.setSizeFull();
        content.addComponent(clearCacheButton);
    }

    private String getBold(String text) {
        return "<b>" + text + "</b>";
    }
}
