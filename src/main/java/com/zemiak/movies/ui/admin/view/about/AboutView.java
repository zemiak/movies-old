package com.zemiak.movies.ui.admin.view.about;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.zemiak.movies.MoviesTheme;
import com.zemiak.movies.boundary.BatchRunner;
import com.zemiak.movies.boundary.GenreService;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.boundary.SerieService;
import com.zemiak.movies.ui.admin.ViewAbstract;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

@CDIView(AboutView.VIEW_ID)
public class AboutView extends ViewAbstract {
    public static final String VERSION = "1.1";
    public static final String VIEW_ID = "about";
    public static final ThemeResource ICON = new ThemeResource("icons/settings.png");
    
    @Inject private SerieService series;
    @Inject private MovieService movies;
    @Inject private GenreService genres;
    @Inject private BatchRunner runner;
    
    private Button runBatch;

   
    public AboutView() {
    }
    
    @PostConstruct
    public void init() {
        initCaption();
    }
    
    private void initCaption() {
        Label head = new Label("About / Settings");
        head.addStyleName(MoviesTheme.LABEL_H1);
        addComponent(head);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        refreshData();
    }

    @Override
    public boolean supports(Class<?> adapterClass) {
        return false;
    }

    @Override
    public <T> T adapt(Class<T> adapterClass) {
        return null;
    }

    private void refreshData() {
        this.removeAllComponents();
        
        initCaption();
        Table table = new Table("Statistics");
        table.addContainerProperty(1, String.class, null);
        table.addContainerProperty(2, Label.class, null);
        table.setColumnAlignment(2, Table.Align.RIGHT);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        table.setWidth("100%");
        table.setHeight("10em");
        
        table.addItem(new Object[]{"Version", 
            new Label(getBold(VERSION), ContentMode.HTML)}, 1);
        table.addItem(new Object[]{"Genres", 
            new Label(getBold(String.valueOf(genres.all().size())), ContentMode.HTML)}, 2);
        table.addItem(new Object[]{"Series", 
            new Label(getBold(String.valueOf(series.all().size())), ContentMode.HTML)}, 3);
        table.addItem(new Object[]{"Movies", 
            new Label(getBold(String.valueOf(movies.all().size())), ContentMode.HTML)}, 4);

        this.addComponent(table);

        initBatchButton();
        runBatch.setEnabled(!runner.isUpdateMoviesRunning());
    }

    private void initBatchButton() {
        runBatch = new Button("Refresh Movies Job");
        runBatch.setDisableOnClick(true);
        runBatch.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (runner.isUpdateMoviesRunning()) {
                    Notification.show("The job is still running...");
                } else {
                    runner.runUpdateCollection();
                }
            }
        });
        
        this.addComponent(runBatch);
    }
    
    private String getBold(final String text) {
        return "<b>" + text + "</b>";
    }
}
