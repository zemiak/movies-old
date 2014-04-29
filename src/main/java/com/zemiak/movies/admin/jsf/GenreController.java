package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.admin.jsf.util.JsfUtil.PersistAction;
import com.zemiak.movies.admin.beans.GenreFacade;

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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Named("genreController")
@SessionScoped
public class GenreController implements Serializable {
    private static final Logger LOG = Logger.getLogger(GenreController.class.getName());

    @EJB
    private com.zemiak.movies.admin.beans.GenreFacade ejbFacade;
    private Genre selected;

    public GenreController() {
    }

    public Genre getSelected() {
        return selected;
    }

    public void setSelected(Genre selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private GenreFacade getFacade() {
        return ejbFacade;
    }

    public Genre prepareCreate() {
        selected = new Genre();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setId(null);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("GenreCreated"));
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("GenreUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("GenreDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
        }
    }

    public List<Genre> getItems() {
        return getFacade().findAll();
    }

    private void persist(final PersistAction persistAction, final String successMessage) {
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
                
                if (cause instanceof ConstraintViolationException) {
                    final ConstraintViolationException e = (ConstraintViolationException) cause;
                    
                    for (ConstraintViolation<?> violation: e.getConstraintViolations()) {
                        LOG.log(Level.SEVERE, "Constraint violation: " 
                                + violation.getPropertyPath() + ": "
                                + violation.getMessage());
                    }
                }
                
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
                
                LOG.log(Level.SEVERE, "EJB exception", cause);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "General exception", ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Genre getGenre(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Genre> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Genre> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Genre.class)
    public static class GenreControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GenreController controller = (GenreController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "genreController");
            return controller.getGenre(getKey(value));
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
            if (object instanceof Genre) {
                Genre o = (Genre) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Genre.class.getName()});
                return null;
            }
        }

    }

}
