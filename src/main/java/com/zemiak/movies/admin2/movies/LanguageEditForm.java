package com.zemiak.movies.admin2.movies;

import com.zemiak.movies.domain.Language;
import com.zemiak.movies.service.LanguageService;
import java.io.Serializable;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("languageEditForm")
public class LanguageEditForm implements Serializable {
    private String id;
    private Language bean;

    @Inject
    private LanguageService service;

    public LanguageEditForm() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

        if (isNew()) {
            create();
        } else {
            refresh();
        }
    }

    private void refresh() {
        bean = service.find(id);

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find language #" + id);
        }
    }

    public Language getBean() {
        return bean;
    }

    private void create() {
        bean = Language.create();
    }

    public String save() {
        service.save(bean);
        JsfMessages.addSuccessMessage("The language has been saved");

        return close();
    }

    public String close() {
        return "index";
    }

    public String remove() {
        try {
            service.remove(id);
            JsfMessages.addSuccessMessage("The language has been deleted");
        } catch (EJBException ex) {
            JsfMessages.addErrorMessage(ex);
        }

        return close();
    }

    public String getActionTitle() {
        return isNew() ? "Create" : "Edit";
    }

    public boolean isNew() {
        return null == id || id.isEmpty() || id.equals("-1");
    }
}
