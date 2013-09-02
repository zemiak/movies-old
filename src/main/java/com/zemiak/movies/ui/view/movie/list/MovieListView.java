package com.zemiak.movies.ui.view.movie.list;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
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
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.ui.view.ViewAbstract;
import com.zemiak.movies.ui.view.movie.MovieForm;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.tepi.filtertable.FilterTable;

@CDIView(MovieListView.VIEW_ID)
public class MovieListView extends ViewAbstract {

    public static final String VIEW_ID = "movie";
    public static final ThemeResource ICON = new ThemeResource("icons/video.png");
    private MovieListFilter filter = MovieListFilter.NEW;
    
    @Inject private MovieService service;
    @Inject private SerieService serieService;
    @Inject private GenreService genreService;
    
    @Inject private javax.enterprise.event.Event<MovieListRefreshEvent> events;
    
    @Inject private Instance<MovieForm> form;
    
    private FilterTable table;
    private IndexedContainer container;

    public MovieListView() {
    }

    @PostConstruct
    public void init() {
        setSizeFull();
        setMargin(true);

        initLabel();
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
                addItems(service.all());
            }
        });
    }

    private void addRow(Movie entity) {
        int id = entity.hashCode();
        Item newItem = container.addItem(id);

        newItem.getItemProperty("ID").setValue(entity.getId());
        newItem.getItemProperty("Name").setValue(entity.getName());
        newItem.getItemProperty("Display Order").setValue(entity.getDisplayOrder());
        
        newItem.getItemProperty("Genre").setValue(null == entity.getGenreId() ? "" : entity.getGenreId().getName());
        newItem.getItemProperty("Serie").setValue(null == entity.getSerieId() ? "" : entity.getSerieId().getName());
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
        table = new FilterTable();
        table.setWidth("100%");
        table.setHeight("100%");
        table.addStyleName("catalog-table");
        table.setSelectable(true);
        table.setMultiSelect(false);

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
        container.addContainerProperty("Genre", String.class, null);
        container.addContainerProperty("Serie", String.class, null);
        table.setContainerDataSource(container);
    }

    private void initButtonBar() {
        Button button;
        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);

        button = new NativeButton("Edit", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editItem();
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);

        button = new NativeButton("New", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                MovieForm f = MovieListView.this.form.get();
                f.setEntity(null);
                getUI().addWindow(f);
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);

        button = new NativeButton("Delete", new Button.ClickListener() {
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

        button = new NativeButton("Order Up", new Button.ClickListener() {
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

        button = new NativeButton("Order Down", new Button.ClickListener() {
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
        int i = 0;
        for (Movie entry : list) {
            addRow(entry);
        }
        System.err.println("Done adding " + list.size() + " items.");
    }
    
    private void editItem() {
        editItem((Integer) table.getValue());
    }

    private void editItem(Integer id) {
        if (null != id) {
            Integer entityId = (Integer) container.getItem(id).getItemProperty("ID").getValue();
            MovieForm f = MovieListView.this.form.get();
            f.setEntity(entityId);
            getUI().addWindow(f);
        } else {
            Notification.show("Select an item, first.", Notification.Type.HUMANIZED_MESSAGE);
        }
    }
}
