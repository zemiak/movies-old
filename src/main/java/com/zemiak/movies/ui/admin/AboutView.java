package com.zemiak.movies.ui.admin;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

@CDIView(AboutView.VIEW_ID)
public class AboutView extends ViewAbstract {
    public static final String VIEW_ID = "aboutAdmin";
    public static final ThemeResource ICON = new ThemeResource("icons/stats.png");
    
    public AboutView() {
    }
    
    private String getBold(String text) {
        return "<b>" + text + "</b>";
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Table table = new Table("Statistics");
        table.addContainerProperty(1, String.class, null);
        table.addContainerProperty(2, Label.class, null);
        table.setColumnAlignment(2, Table.Align.RIGHT);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        table.setWidth("100%");
        table.setHeight("10em");
        
        table.addItem(new Object[]{"Version", 
            new Label(getBold("1.0"), ContentMode.HTML)}, 1);

        addComponent(table);
    }

    @Override
    public boolean supports(Class<?> adapterClass) {
        return false;
    }

    @Override
    public <T> T adapt(Class<T> adapterClass) {
        return null;
    }
}
