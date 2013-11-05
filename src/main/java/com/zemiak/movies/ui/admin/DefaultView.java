package com.zemiak.movies.ui.admin;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.zemiak.movies.MoviesTheme;

@CDIView
public class DefaultView extends ViewAbstract {
    public DefaultView() {
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {setSpacing(true);
        setMargin(true);
        setSizeFull();

        Label title = new Label("Please, select one module from the left side.");
        title.addStyleName(MoviesTheme.LABEL_H1);
        addComponent(title);
    }

    @Override
    public boolean supports(Class<?> adapterClass) {
        return false;
    }

    @Override
    public <T> T adapt(Class<T> adapterClass) {
        return null;
    }
}
