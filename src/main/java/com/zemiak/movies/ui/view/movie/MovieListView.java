package com.zemiak.movies.ui.view.movie;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.zemiak.movies.MoviesTheme;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.boundary.SerieService;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.ui.view.ViewAbstract;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@CDIView(MovieListView.VIEW_ID)
public class MovieListView extends ViewAbstract {
    public static final String VIEW_ID = "movie";
    public static final ThemeResource ICON = new ThemeResource("icons/video.png");
    
    private MovieListFilter filter = MovieListFilter.NEW;
    private ComboBox serie, genre;
    private Button newMoviesButton;
    
    @Inject
    private MovieService service;
    
    @Inject
    private GenreService genreService;
    
    @Inject
    private SerieService serieService;
    
    @Inject
    Instance<MovieForm> form;
    
    @Inject
    private javax.enterprise.event.Event<MovieListRefreshEvent> events;
    
    private Table table;
    private IndexedContainer container;

    public MovieListView() {
    }
    
    @PostConstruct
    public void init() {
        setSizeFull();
        setMargin(true);
        
        initLabel();
        initFilters();
        initTable();
        initContainer(table);
        initButtonBar();
        
        table.setVisibleColumns((Object[]) new String[]{"ID", "Name", "Display Order", "Genre", "Serie"});
        setExpandRatio(table, 1);
    }
    
    public void refreshContainer(@Observes MovieListRefreshEvent event) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                container.removeAllItems();
        
                switch (filter) {
                    case NEW:
                        addItems(service.getNewMovies());
                        break;
                    case GENRE:
                        addItems(service.getGenreMovies(genreService.find((Integer) genre.getValue())));
                        break;
                    case SERIE:
                        addItems(service.getSerieMovies(serieService.find((Integer) serie.getValue())));
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        });
    }
    
    private void addRow(Movie entity) {
        int id = entity.hashCode();
        Item newItem = container.addItem(id);
        
        newItem.getItemProperty("ID").setValue(entity.getId());
        newItem.getItemProperty("Name").setValue(entity.getName());
        newItem.getItemProperty("Display Order").setValue(entity.getDisplayOrder());
        newItem.getItemProperty("Genre").setValue(entity.getGenreId().getName());
        newItem.getItemProperty("Serie").setValue(entity.getSerieId().getName());
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
        container.addContainerProperty("ID", Integer.class, null);
        container.addContainerProperty("Name", String.class, null);
        container.addContainerProperty("Display Order", Integer.class, null);
        container.addContainerProperty("Genre", String.class, null);
        container.addContainerProperty("Serie", String.class, null);
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
                    Integer entityId = (Integer) container.getItem(id).getItemProperty("ID").getValue();
                    MovieForm form = MovieListView.this.form.get();
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
                MovieForm form = MovieListView.this.form.get();
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
                    Integer entityId = (Integer) container.getItem(id).getItemProperty("ID").getValue();
                    service.remove(entityId);
                    refreshContainer(null);
                } else {
                    Notification.show("Select an item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
        
        button = new NativeButton("Order Up", new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Integer id = (Integer) table.getValue();
                
                if (null != id) {
                    Integer entityId = (Integer) container.getItem(id).getItemProperty("ID").getValue();
                    Movie entity = service.find(entityId);
                    entity.setDisplayOrder(entity.getDisplayOrder() - 1);
                    service.save(entity);
                    refreshContainer(null);
                } else {
                    Notification.show("Select an item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        button.setIcon(new ThemeResource("icons/arrow_up.png"));
        buttonBar.addComponent(button);
        
        button = new NativeButton("Order Down", new Button.ClickListener(){
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Integer id = (Integer) table.getValue();
                
                if (null != id) {
                    Integer entityId = (Integer) container.getItem(id).getItemProperty("ID").getValue();
                    Movie entity = service.find(entityId);
                    entity.setDisplayOrder(entity.getDisplayOrder() + 1);
                    service.save(entity);
                    refreshContainer(null);
                } else {
                    Notification.show("Select an item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        button.setIcon(new ThemeResource("icons/arrow_down.png"));
        buttonBar.addComponent(button);
        
        addComponent(buttonBar);
    }
    
    private void initLabel() {
        Label head = new Label("Movies");
        head.addStyleName(MoviesTheme.LABEL_H1);
        addComponent(head);
    }
    
    private void addItems(List<Movie> list) {
        for (Movie entry: list) {
            addRow(entry);
        }
    }

    private void initFilters() {
        initNewMoviesFilter();
        initGenreFilter();
        initSerieFilter();
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponents(newMoviesButton, genre, serie);
        addComponent(layout);
        
        setComponentAlignment(layout, Alignment.MIDDLE_RIGHT);
    }

    private void initNewMoviesFilter() {
        newMoviesButton = new Button("New Movies", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                filter = MovieListFilter.NEW;
                events.fire(new MovieListRefreshEvent());
            }
        });
    }

    private void initGenreFilter() throws UnsupportedOperationException {
        genre = new ComboBox("Genre");
        genre.setWidth("10em");
        for (Genre entry: genreService.all()) {
            genre.addItem(entry.getId());
            genre.setItemCaption(entry.getId(), entry.getName());
        }
        genre.addValueChangeListener(new ComboBox.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                filter = MovieListFilter.GENRE;
                events.fire(new MovieListRefreshEvent());
            }
        });
    }

    private void initSerieFilter() throws UnsupportedOperationException {
        serie = new ComboBox("Serie");
        serie.setWidth("10em");
        for (Serie entry: serieService.all()) {
            serie.addItem(entry.getId());
            serie.setItemCaption(entry.getId(), entry.getName());
        }
        serie.addValueChangeListener(new ComboBox.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                filter = MovieListFilter.SERIE;
                events.fire(new MovieListRefreshEvent());
            }
        });
    }
}
