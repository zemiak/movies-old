package com.zemiak.movies.ui.view.admin.language;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.zemiak.movies.MoviesTheme;
import com.zemiak.movies.boundary.LanguageService;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.ui.view.ViewAbstract;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@CDIView(LanguageListView.VIEW_ID)
public class LanguageListView extends ViewAbstract {
    public static final String VIEW_ID = "language";
    public static final ThemeResource ICON = new ThemeResource("icons/world.png");
    
    @Inject
    private LanguageService service;
    
    @Inject
    Instance<LanguageForm> form;
    
    private Table table;
    private IndexedContainer container;

    public LanguageListView() {
    }
    
    @PostConstruct
    public void init() {
        setSizeFull();
        setMargin(true);

        initLabel();
        initTable();
        initContainer(table);
        initButtonBar();
        
        table.setVisibleColumns((Object[]) new String[]{"ID", "Name"});
        setExpandRatio(table, 1);
    }
    
    public void refreshContainer(@Observes LanguageListRefreshEvent event) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                container.removeAllItems();
        
                for (Language entry: service.all()) {
                    addRow(entry);
                }
            }
        });
    }
    
    private void addRow(Language entity) {
        int id = entity.hashCode();
        Item newItem = container.addItem(id);
        newItem.getItemProperty("ID").setValue(entity.getId());
        newItem.getItemProperty("Name").setValue(entity.getName());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshContainer(null);
    }

    @Override
    public boolean supports(Class<?> adapterClass) {
        return false;
    }

    @Override
    public <T> T adapt(Class<T> adapterClass) {
        return null;
    }

    private void initTable() {
        table = new Table();
        table.setWidth("100%");
        table.setHeight("100%");
        table.addStyleName("catalog-table");
        table.setSelectable(true);
        table.setMultiSelect(false);
        addComponent(table);
    }

    private void initContainer(Table table) {
        container = new IndexedContainer();
        container.addContainerProperty("ID", String.class, null);
        container.addContainerProperty("Name", String.class, null);
        table.setContainerDataSource(container);
    }

    private void initButtonBar() {
        Button button;
        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);
        
        button = new NativeButton("Edit", new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Integer id = (Integer) table.getValue();
                
                if (null != id) {
                    String entityId = (String) container.getItem(id).getItemProperty("ID").getValue();
                    LanguageForm form = LanguageListView.this.form.get();
                    form.setEntity(entityId);                    
                    getUI().addWindow(form);
                } else {
                    Notification.show("Select an item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
        
        button = new NativeButton("New", new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent event) {
                LanguageForm form = LanguageListView.this.form.get();
                form.setEntity(null);
                getUI().addWindow(form);
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
        
        button = new NativeButton("Delete", new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Integer id = (Integer) table.getValue();
                
                if (null != id) {
                    String entityId = (String) container.getItem(id).getItemProperty("ID").getValue();
                    service.remove(entityId);
                    refreshContainer(null);
                } else {
                    Notification.show("Select an item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
        
        addComponent(buttonBar);
    }
    
    private void initLabel() {
        Label head = new Label("Languages");
        head.addStyleName(MoviesTheme.LABEL_H1);
        addComponent(head);
    }
}
