package com.zemiak.movies.ui.view.admin.language;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.zemiak.movies.boundary.LanguageService;
import com.zemiak.movies.domain.Language;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author vasko
 */
@Dependent
public class LanguageForm extends Window {

    private FormLayout layout;
    private Language entity;
    TextField id, name;
    private FieldGroup binder;
    
    @Inject
    private LanguageService service;
    
    @Inject
    private javax.enterprise.event.Event<LanguageListRefreshEvent> events;
    
    public LanguageForm() {
    }

    public void setEntity(String entityId) {
        if (null == entityId) {
            entity = new Language();
        } else {
            entity = service.find(entityId);
        }
        
        id.setValue(entity.getId());
        name.setValue(entity.getName());
    }

    @PostConstruct
    public void init() {
        this.center();
        this.setHeight("14em");
        this.setWidth("35em");
        
        initLayout();
        initFields();
        initButtons();
    }
    
    @Override
    public void attach() {
        super.attach();
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
    }
    
    @Override
    public void detach() {
        super.detach();
    }

    private void initFields() {
        id = new TextField("Code");
        id.setWidth("100%");
        id.addStyleName("catalog-form");
        layout.addComponent(id);
        
        name = new TextField("Name");
        name.setWidth("100%");
        name.addStyleName("catalog-form");
        name.focus();
        layout.addComponent(name);
        
        binder = new FieldGroup();
        binder.bind(id, "ID");
        binder.bind(name, "Name");
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
                    if (! binder.isValid()) {
                        throw new FieldGroup.CommitException("");
                    }
                    
                    entity.setId(id.getValue());
                    entity.setName(name.getValue());
                    
                    service.save(entity);
                    
                    events.fire(new LanguageListRefreshEvent());
                    Notification.show("The language has been saved.", Notification.Type.HUMANIZED_MESSAGE);
                    close();
                } catch (FieldGroup.CommitException e) {
                    Notification.show("Validation error", Notification.Type.WARNING_MESSAGE);
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    throw e;
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
