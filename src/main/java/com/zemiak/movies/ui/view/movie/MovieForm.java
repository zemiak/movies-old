package com.zemiak.movies.ui.view.movie;

import com.zemiak.movies.ui.view.movie.list.MovieListRefreshEvent;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.boundary.LanguageService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.boundary.SerieService;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author vasko
 */
@Dependent
public class MovieForm extends Window {

    private FormLayout layout;
    private Movie entity;
    private TextField name, order, originalName, url;
    private ComboBox genre, serie, language, subtitles, originalLanguage;
    private TextArea description;
    FieldGroup binder;
    
    @Inject
    private MovieService service;
    
    @Inject
    private GenreService genreService;
    
    @Inject
    private SerieService serieService;
    
    @Inject
    private LanguageService languageService;
    
    @Inject
    private javax.enterprise.event.Event<MovieListRefreshEvent> events;
    
    public MovieForm() {
    }

    public void setEntity(Integer entityId) {
        if (null == entityId) {
            entity = new Movie();
        } else {
            entity = service.find(entityId);
        }
        
        name.setValue(entity.getName());
        order.setValue(String.valueOf(entity.getDisplayOrder()));
        genre.setValue(null == entity.getGenreId() ? null : entity.getGenreId().getId());
        serie.setValue(null == entity.getSerieId() ? null : entity.getSerieId().getId());
        originalLanguage.setValue(null == entity.getOriginalLanguage() ? null : entity.getOriginalLanguage().getId());
        language.setValue(null == entity.getLanguage() ? null : entity.getLanguage().getId());
        subtitles.setValue(null == entity.getSubtitles() ? null : entity.getSubtitles().getId());
        originalName.setValue(entity.getOriginalName());
        url.setValue(entity.getUrl());
        description.setValue(entity.getDescription());
    }

    @PostConstruct
    public void init() {
        this.center();
        this.setHeight("35em");
        this.setWidth("45em");
        
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
        initNameField();
        initOriginalNameField();
        initOrderField();
        initGenreSerieCombos();
        initLanguageCombos();
        initUrlField();
        initDescriptionField();
        initBinder();
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
                    
                    entity.setName(name.getValue());
                    entity.setDisplayOrder(Integer.valueOf(order.getValue()));
                    entity.setGenreId(genreService.find((Integer) genre.getValue()));
                    entity.setSerieId(serieService.find((Integer) serie.getValue()));
                    entity.setOriginalName(originalName.getValue());
                    entity.setUrl(url.getValue());
                    entity.setLanguage(languageService.find((String) language.getValue()));
                    entity.setSubtitles(languageService.find((String) subtitles.getValue()));
                    entity.setOriginalLanguage(languageService.find((String) originalLanguage.getValue()));
                    entity.setDescription(description.getValue());
                    service.save(entity);
                    
                    events.fire(new MovieListRefreshEvent());
                    Notification.show("The movie has been saved.", Notification.Type.HUMANIZED_MESSAGE);
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

    private void initGenreSerieCombos() {
        HorizontalLayout comboLayout = new HorizontalLayout();
        
        genre = new ComboBox("Genre");
        genre.setWidth("10em");
        for (Genre entry: genreService.all()) {
            genre.addItem(entry.getId());
            genre.setItemCaption(entry.getId(), entry.getName());
        }
        
        serie = new ComboBox("Serie");
        serie.setWidth("10em");
        for (Serie entry: serieService.all()) {
            serie.addItem(entry.getId());
            serie.setItemCaption(entry.getId(), entry.getName());
        }
        
        comboLayout.addComponents(genre, serie);
        layout.addComponent(comboLayout);
    }
    
    private void initLanguageCombos() {
        HorizontalLayout comboLayout = new HorizontalLayout();
        
        language = new ComboBox("Language");
        language.setWidth("10em");
        
        originalLanguage = new ComboBox("Original Language");
        originalLanguage.setWidth("10em");
        
        subtitles = new ComboBox("Subtitles");
        subtitles.setWidth("10em");
        
        for (Language entry: languageService.all()) {
            language.addItem(entry.getId());
            language.setItemCaption(entry.getId(), entry.getName());
            
            originalLanguage.addItem(entry.getId());
            originalLanguage.setItemCaption(entry.getId(), entry.getName());
            
            subtitles.addItem(entry.getId());
            subtitles.setItemCaption(entry.getId(), entry.getName());
        }
        
        comboLayout.addComponents(language, originalLanguage, subtitles);
        layout.addComponent(comboLayout);
    }

    private void initNameField() {
        name = new TextField("Name");
        name.setWidth("100%");
        name.addStyleName("catalog-form");
        name.focus();
        layout.addComponent(name);
    }
    
    private void initUrlField() {
        url = new TextField("URL");
        url.setWidth("100%");
        url.addStyleName("catalog-form");
        url.focus();
        layout.addComponent(url);
    }
    
    private void initDescriptionField() {
        description = new TextArea("Description");
        description.setWidth("100%");
        description.addStyleName("catalog-form");
        description.focus();
        layout.addComponent(description);
    }
    
    private void initOriginalNameField() {
        originalName = new TextField("Original Name");
        originalName.setWidth("100%");
        originalName.addStyleName("catalog-form");
        originalName.focus();
        layout.addComponent(originalName);
    }

    private void initOrderField() {
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

    private void initBinder() throws FieldGroup.BindException {
        binder = new FieldGroup();
        binder.bind(name, "Name");
        binder.bind(order, "Order");
        binder.bind(genre, "Genre");
        binder.bind(serie, "Serie");
        binder.bind(originalName, "OriginalName");
        binder.bind(url, "Url");
        binder.bind(language, "Language");
        binder.bind(subtitles, "Subtitles");
        binder.bind(originalLanguage, "OriginalLanguage");
        binder.bind(description, "Description");
    }
}
