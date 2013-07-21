package com.zemiak.movies.ui.view;

import com.zemiak.movies.MoviesTheme;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import javax.annotation.PostConstruct;

/**
 * @author peholmst
 */
@UIScoped
public class ErrorView extends VerticalLayout implements View {

    private Label details;

    @PostConstruct
    void init() {
        setSpacing(true);
        setMargin(true);

        final Image icon = new Image(null, new ThemeResource("icons/error_48.png"));
        final Label header = new Label("The view was not found");
        header.addStyleName(MoviesTheme.LABEL_H1);
        addComponent(new HorizontalLayout(icon, header) {{
            setSpacing(true);
            setComponentAlignment(icon, Alignment.MIDDLE_LEFT);
            setComponentAlignment(header, Alignment.MIDDLE_LEFT);
        }});

        details = new Label();
        addComponent(details);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        details.setValue(String.format("Tried to navigate to a view '%s' that does not exist."
                + " This could be a bug in the software. Please try again, and if the problem"
                +" persists, contact the system administrator.", event.getViewName()));
    }

}
