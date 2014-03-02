package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.domain.Serie;
import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.admin.jsf.util.JsfUtil.PersistAction;
import com.zemiak.movies.admin.beans.SerieFacade;

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

@Named("serieController")
@SessionScoped
public class SerieController implements Serializable {

    @EJB
    private com.zemiak.movies.admin.beans.SerieFacade ejbFacade;
    private Serie selected;

    public SerieController() {
    }

    public Serie getSelected() {
        return selected;
    }

    public void setSelected(Serie selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SerieFacade getFacade() {
        return ejbFacade;
    }

    public Serie prepareCreate() {
        selected = new Serie();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setId(null);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SerieCreated"));
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SerieUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SerieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
        }
    }

    public List<Serie> getItems() {
        return getFacade().findAll();
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                switch (persistAction) {
                    case CREATE:
                        getFacade().create(selected);
                        break;
                    case UPDATE:
                        getFacade().edit(selected);
                        break;
                    case DELETE:
                        getFacade().remove(selected);
                        break;
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

    public Serie getSerie(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Serie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Serie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Serie.class)
    public static class SerieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SerieController controller = (SerieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "serieController");
            return controller.getSerie(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Serie) {
                Serie o = (Serie) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Serie.class.getName()});
                return null;
            }
        }

    }

}
