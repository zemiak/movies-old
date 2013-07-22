package com.zemiak.movies.ui.view.admin.genre;

import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.ui.view.UrlData;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author vasko
 */
@Dependent
public class GenreForm extends Window {

    private FormLayout layout;
    private Genre entity;
    private TextField id, name, order;
    
    private Embedded image;
    private Layout panelContent;
    
    @Inject
    private GenreService service;
    
    @Inject
    private javax.enterprise.event.Event<GenreListRefreshEvent> events;
    
    public GenreForm() {
    }

    public void setEntity(Integer entityId) {
        if (null == entityId) {
            entity = new Genre();
            
            image = new Embedded("Icon");
            image.setVisible(false);
        } else {
            entity = service.find(entityId);
            
            image = new Embedded("Icon", 
                new ExternalResource(UrlData.IMG_PATH + "genre/"
                + entity.getPictureFileName()));
        }
        
        id.setValue(String.valueOf(entity.getId()));
        name.setValue(entity.getName());
        order.setValue(String.valueOf(entity.getDisplayOrder()));
        
        panelContent.removeAllComponents();
        panelContent.addComponent(image);
    }

    @PostConstruct
    public void init() {
        this.center();
        this.setHeight("30em");
        this.setWidth("30em");
        
        initLayout();
        initFields();
        initButtons();
    }
    
    @Override
    public void attach() {
        super.attach();
        setCloseShortcut(ShortcutListener.KeyCode.ESCAPE);
    }
    
    @Override
    public void detach() {
        super.detach();
    }

    private void initFields() {
        id = new TextField("ID");
        id.setWidth("100%");
        id.addStyleName("catalog-form");
        layout.addComponent(id);
        
        name = new TextField("Name");
        name.setWidth("100%");
        name.addStyleName("catalog-form");
        name.focus();
        layout.addComponent(name);
        
        image = new Embedded("Icon");
        image.setVisible(false);

        panelContent = new VerticalLayout();
        layout.addComponent(panelContent);
        
        order = new TextField("Order");
        order.addStyleName("catalog-form");
        order.addValidator(new Validator() {
            @Override
            public void validate(Object value) throws Validator.InvalidValueException {
                String val = (String) value;
                Integer real;
                
                try {
                    real = Integer.valueOf(val);
                } catch (NumberFormatException ex) {
                    throw new Validator.InvalidValueException("Cannot convert to Integer");
                }
            }
        });
        layout.addComponent(order);
    }

    private void initLayout() {
        layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        setContent(layout);
    }

    private void initButtons() {
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSpacing(true);
        
        Button button = new NativeButton("Save", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    entity.setId(Integer.valueOf(id.getValue()));
                    entity.setName(name.getValue());
                    entity.setDisplayOrder(Integer.valueOf(order.getValue()));
                    service.save(entity);
                    
                    events.fire(new GenreListRefreshEvent());
                    Notification.show("The language has been saved.", Notification.Type.HUMANIZED_MESSAGE);
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    throw e;
                } finally {
                    close();
                }
            }
        });
        button.addStyleName("catalog-form");
        hlayout.addComponent(button);
        
        button = new NativeButton("Close", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        button.addStyleName("catalog-form");
        hlayout.addComponent(button);
        
        layout.addComponent(hlayout);
    }
}
