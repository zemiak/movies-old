package com.zemiak.movies.ui.view.movie.list;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.zemiak.movies.MoviesTheme;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.boundary.SerieService;
import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Language;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.ui.view.ViewAbstract;
import com.zemiak.movies.ui.view.movie.MovieForm;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.tepi.filtertable.FilterTable;

@CDIView(MovieListView.VIEW_ID)
public class MovieListView extends ViewAbstract {

    public static final String VIEW_ID = "movie";
    public static final ThemeResource ICON = new ThemeResource("icons/video.png");
    MovieListFilter filter = MovieListFilter.NEW;
    
    @Inject private MovieService service;
    @Inject private SerieService serieService;
    @Inject private GenreService genreService;
    
    @Inject javax.enterprise.event.Event<MovieListRefreshEvent> events;
    
    @Inject MovieForm form;
    
    FilterTable table;
    IndexedContainer container;
    
    @Inject
    MovieListButtons buttons;

    public MovieListView() {
    }

    @PostConstruct
    public void init() {
        setSizeFull();
        setMargin(true);

        initLabel();
        initFilterBar();
        initTable();
        initContainer(table);
        buttons.setView(this).initialize();

        table.setVisibleColumns((Object[]) new String[]{"ID", "Name", "Display Order", "Genre", "Serie"});
        setExpandRatio(table, 1);
    }

    public void refreshContainer(@Observes final MovieListRefreshEvent event) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                container.removeAllItems();
                
                if (null != event && event.isOnlyNew()) {
                    addItems(service.getNewMovies());
                } else {
                    addItems(service.all());
                }
                
            }
        });
    }

    void addRow(Movie entity) {
        int id = entity.hashCode();
        Item newItem = container.addItem(id);

        newItem.getItemProperty("ID").setValue(entity.getId());
        newItem.getItemProperty("Name").setValue(entity.getName());
        newItem.getItemProperty("Display Order").setValue(entity.getDisplayOrder());
        
        newItem.getItemProperty("Genre").setValue(entity.getGenreId());
        newItem.getItemProperty("Serie").setValue(entity.getSerieId());
        
        newItem.getItemProperty("Language").setValue(entity.getLanguage());
        newItem.getItemProperty("Subtitles").setValue(entity.getSubtitles());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!service.getNewMovies().isEmpty()) {
            refreshContainer(new MovieListRefreshEvent(true));
        } else {
            refreshContainer(null);
            Notification.show("There are no new movies, showing all of them.");
        }
        
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
        table = new FilterTable();
        table.setWidth("100%");
        table.setHeight("100%");
        table.addStyleName("catalog-table");
        table.setSelectable(true);
        table.setMultiSelect(true);

        table.setFilterBarVisible(true);
        table.setFilterGenerator(new CustomFilterGenerator(
                genreService.all(), serieService.all()));
        
        table.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    editItem((Integer) event.getItemId());
                }
            }
        });

        addComponent(table);
    }

    private void initContainer(FilterTable table) {
        container = new IndexedContainer();
        container.addContainerProperty("ID", Integer.class, null);
        container.addContainerProperty("Name", String.class, null);
        container.addContainerProperty("Display Order", Integer.class, null);
        container.addContainerProperty("Genre", Genre.class, null);
        container.addContainerProperty("Serie", Serie.class, null);
        container.addContainerProperty("Language", Language.class, null);
        container.addContainerProperty("Subtitles", Language.class, null);
        table.setContainerDataSource(container);
    }

    

    private void initLabel() {
        Label head = new Label("Movies");
        head.addStyleName(MoviesTheme.LABEL_H1);
        addComponent(head);
    }

    private void addItems(List<Movie> list) {
        int i = 0;
        for (Movie entry : list) {
            addRow(entry);
        }
        System.err.println("Done adding " + list.size() + " items.");
    }
    
    void editItem() {
        editItem((Integer) table.getValue());
    }

    void editItem(Integer id) {
        if (null != id) {
            Integer entityId = (Integer) container.getItem(id).getItemProperty("ID").getValue();
            MovieForm f = MovieListView.this.form;
            f.setEntity(entityId);
            getUI().addWindow(f);
        } else {
            Notification.show("Select an item, first.", Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private void initFilterBar() {
        Button button;
        HorizontalLayout filterBar = new HorizontalLayout();
        filterBar.setSpacing(true);

        button = new NativeButton("All", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                refreshContainer(null);
            }
        });
        filterBar.addComponent(button);
        
        button = new NativeButton("New", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                refreshContainer(new MovieListRefreshEvent(true));
            }
        });
        filterBar.addComponent(button);
        
        addComponent(filterBar);
        this.setComponentAlignment(filterBar, Alignment.BOTTOM_RIGHT);
    }
}
