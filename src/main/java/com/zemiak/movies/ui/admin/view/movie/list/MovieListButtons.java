package com.zemiak.movies.ui.admin.view.movie.list;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.zemiak.movies.boundary.MovieService;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.ui.admin.view.movie.MovieForm;
import com.zemiak.movies.ui.admin.view.movie.list.dialog.AbstractEditWindow;
import com.zemiak.movies.ui.admin.view.movie.list.dialog.GenreEditWindow;
import com.zemiak.movies.ui.admin.view.movie.list.dialog.LanguageEditWindow;
import com.zemiak.movies.ui.admin.view.movie.list.dialog.OriginalLanguageEditWindow;
import com.zemiak.movies.ui.admin.view.movie.list.dialog.SerieEditWindow;
import com.zemiak.movies.ui.admin.view.movie.list.dialog.SubtitlesEditWindow;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author vasko
 */
@Dependent
public class MovieListButtons {
    private MovieListView view;
    private HorizontalLayout buttonBar;
    
    @Inject private MovieService service;
    
    @Inject private GenreEditWindow genreEditWindow;
    @Inject private SerieEditWindow serieEditWindow;
    @Inject private LanguageEditWindow languageEditWindow;
    @Inject private OriginalLanguageEditWindow originalLanguageEditWindow;
    @Inject private SubtitlesEditWindow subtitlesEditWindow;

    public MovieListButtons setView(MovieListView view) {
        this.view = view;
        
        return this;
    }
    
    public void initialize() {
        buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);
        
        initEditButton();
        initNewButton();
        initDeleteButton();
        initOrderUpButton();
        initOrderDownButton();
        initGenreButton();
        initSerieButton();
        initLanguageButton();
        initOriginalLanguageButton();
        initSubtitleButton();
        
        view.addComponent(buttonBar);
    }
    
    public MovieListButtons() {
        
    }
    
    private void initEditButton() {
        Button button;
        button = new NativeButton("Edit", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                view.editItem();
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
    }

    private void initNewButton() {
        Button button;
        button = new NativeButton("New", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                MovieForm f = view.form;
                f.setEntity(null);
                view.getUI().addWindow(f);
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
    }

    private void initDeleteButton() {
        Button button;
        button = new NativeButton("Delete", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Integer id = getOneSelection();

                if (null != id) {
                    Integer entityId = (Integer) view.container.getItem(id).getItemProperty("ID").getValue();
                    service.remove(entityId);
                    view.refreshContainer(null);
                } else {
                    Notification.show("Select exactly one item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
    }

    private void initOrderUpButton() {
        Button button;
        button = new NativeButton("Order Up", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Integer id = getOneSelection();

                if (null != id) {
                    Integer entityId = (Integer) view.container.getItem(id).getItemProperty("ID").getValue();
                    Movie entity = service.find(entityId);
                    entity.setDisplayOrder(entity.getDisplayOrder() - 1);
                    service.save(entity);
                    view.refreshContainer(null);
                } else {
                    Notification.show("Select exactly one item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        button.setIcon(new ThemeResource("icons/arrow_up.png"));
        buttonBar.addComponent(button);
    }

    private void initOrderDownButton() {
        Button button;
        button = new NativeButton("Order Down", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Integer id = getOneSelection();

                if (null != id) {
                    Integer entityId = (Integer) view.container.getItem(id).getItemProperty("ID").getValue();
                    Movie entity = service.find(entityId);
                    entity.setDisplayOrder(entity.getDisplayOrder() + 1);
                    service.save(entity);
                    view.refreshContainer(null);
                } else {
                    Notification.show("Select exactly one item, first.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        });
        button.addStyleName("catalog-table");
        button.setIcon(new ThemeResource("icons/arrow_down.png"));
        buttonBar.addComponent(button);
    }
    
    private Integer getOneSelection() {
        Set<Integer> selected = (Set<Integer>) view.table.getValue();

        if (selected.size() != 1) {
            return null;
        }

        return (Integer) selected.toArray()[0];
    }
    
    private void initActionButton(String caption, final AbstractEditWindow dialog) {
        Button button;
        button = new NativeButton(caption, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Set<Integer> selected = (Set<Integer>) view.table.getValue();
                if (selected.isEmpty()) {
                    Notification.show("Please, select at least one item.");
                    return;
                }
                
                dialog
                        .setTable(view.table)
                        .run();
            }
        });
        button.addStyleName("catalog-table");
        buttonBar.addComponent(button);
    }

    private void initGenreButton() {
        initActionButton("Genre", genreEditWindow);
    }
    
    private void initSerieButton() {
        initActionButton("Serie", serieEditWindow);
    }

    private void initLanguageButton() {
        initActionButton("Language", languageEditWindow);
    }

    private void initOriginalLanguageButton() {
        initActionButton("Original Language", originalLanguageEditWindow);
    }

    private void initSubtitleButton() {
        initActionButton("Subtitles", subtitlesEditWindow);
    }
}
