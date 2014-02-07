package com.zemiak.movies.ui.admin.view.movie.list.dialog;

import com.vaadin.data.Container;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.zemiak.movies.domain.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.tepi.filtertable.FilterTable;

/**
 *
 * @author vasko
 */
abstract public class AbstractEditWindow extends Window {
    private FilterTable table;
    
    public AbstractEditWindow() {
        super();
    }

    public AbstractEditWindow setTable(FilterTable table) {
        this.table = table;
        
        return this;
    }
    
    abstract public void process(List<Movie> movies);
    
    private List<Movie> getSelected() {
        Set<Integer> selected = (Set<Integer>) table.getValue();
        List<Movie> res = new ArrayList<>();
        Container container = table.getContainerDataSource();
        
        for (Integer id: selected) {
            Integer entityId = (Integer) container.getItem(id).getItemProperty("ID").getValue();
            res.add(getEntityManager().find(Movie.class, entityId));
        }
        
        return res;
    }
    
    abstract protected Component content();
    
    public void run() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        final Window that = this;
        
        Button ok = new Button("OK", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                process(getSelected());
            }
        });
        ok.setClickShortcut(KeyCode.ENTER);
        ok.addStyleName("catalog-table");
        
        Button cancel = new Button("Cancel", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                that.close();
            }
        });
        cancel.setClickShortcut(KeyCode.ESCAPE);
        cancel.addStyleName("catalog-table");
        
        buttonLayout.setSpacing(true);
        buttonLayout.addComponents(ok, cancel);
        
        layout.setMargin(true);
        layout.addComponents(content(), buttonLayout);
        
        that.setContent(layout);
        that.center();
        that.setWidth(20, Unit.EM);
        that.setHeight(10, Unit.EM);
        that.setSizeUndefined();
        
        UI.getCurrent().addWindow(that);
    }

    abstract protected EntityManager getEntityManager();
}
