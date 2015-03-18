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
    private String code;

    @Inject
    private LanguageService service;

    public LanguageEditForm() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String check() {
        if (isNew()) {
            create();
        } else {
            refresh();
        }

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find language #" + id);
            return "index";
        }

        return null;
    }

    private void refresh() {
        bean = service.find(id);

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find language #" + id);
        }

        code = bean.getId();
    }

    public Language getBean() {
        return bean;
    }

    private void create() {
        bean = Language.create();
    }

    public String save() {
        bean.setId(code);
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
        return "-1".equals(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
