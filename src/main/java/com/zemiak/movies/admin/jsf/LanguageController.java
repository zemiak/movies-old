package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.domain.Language;
import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.admin.jsf.util.JsfUtil.PersistAction;
import com.zemiak.movies.admin.beans.LanguageFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("languageController")
@SessionScoped
public class LanguageController implements Serializable {

    @EJB
    private com.zemiak.movies.admin.beans.LanguageFacade ejbFacade;
    private Language selected;

    public LanguageController() {
    }

    public Language getSelected() {
        return selected;
    }

    public void setSelected(Language selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LanguageFacade getFacade() {
        return ejbFacade;
    }

    public Language prepareCreate() {
        selected = new Language();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LanguageCreated"));
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LanguageUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LanguageDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
        }
    }

    public List<Language> getItems() {
        return getFacade().findAll();
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Language getLanguage(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Language> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Language> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Language.class)
    public static class LanguageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LanguageController controller = (LanguageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "languageController");
            return controller.getLanguage(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Language) {
                Language o = (Language) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Language.class.getName()});
                return null;
            }
        }

    }

}
