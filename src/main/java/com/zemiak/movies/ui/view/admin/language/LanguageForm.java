package com.zemiak.movies.ui.view.admin.language;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
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
    private Language lang;
    TextField id, name;
    
    @Inject
    private LanguageService service;
    
    @Inject
    private javax.enterprise.event.Event<LanguageListRefreshEvent> events;
    
    public LanguageForm() {
    }

    public void setLanguage(String entityId) {
        if (null == entityId) {
            lang = new Language();
        } else {
            lang = service.find(entityId);
        }
        
        id.setValue(lang.getId());
        name.setValue(lang.getName());
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
        
        hlayout.addComponent(new Button("Save", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    lang.setId(id.getValue());
                    lang.setName(name.getValue());
                    
                    service.save(lang);
                    
                    events.fire(new LanguageListRefreshEvent());
                    Notification.show("The language has been saved.", Notification.Type.HUMANIZED_MESSAGE);
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    throw e;
                } finally {
                    close();
                }
            }
        }));
        
        hlayout.addComponent(new Button("Close", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        }));
        
        layout.addComponent(hlayout);
    }
}
