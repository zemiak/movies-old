package com.zemiak.movies.admin.jsf;

import com.zemiak.movies.admin.jsf.util.JsfUtil;
import com.zemiak.movies.boundary.BatchLogService;
import com.zemiak.movies.domain.BatchLog;
import com.zemiak.movies.domain.CacheClearEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("batchLogController")
@SessionScoped
public class BatchLogController implements Serializable {
    private static final Logger LOG = Logger.getLogger(BatchLogController.class.getName());
    
    @Inject private BatchLogService service;
    
    private List<BatchLog> items;
    private BatchLog selected;

    public BatchLogController() {
        items = new ArrayList<>();
        selected = null;
    }
    
    @PostConstruct
    public void init() {
        items = service.all();
    }

    public List<BatchLog> getItems() {
        return items;
    }

    public BatchLog getSelected() {
        return selected;
    }

    public void setSelected(BatchLog selected) {
        this.selected = selected;
    }
    
    private void showMessage(final String message) {
        JsfUtil.addSuccessMessage(message);
    }
    
    public BatchLog getItem(final Integer id) {
        return service.find(id);
    }
    
    @FacesConverter(forClass = BatchLog.class)
    public static class BatchLogItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BatchLogController controller = (BatchLogController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "batchLogController");
            return controller.getItem(getKey(value));
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
            if (object instanceof BatchLog) {
                BatchLog o = (BatchLog) object;
                return getStringKey(o.getId());
            } else {
                LOG.log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), BatchLog.class.getName()});
                return null;
            }
        }
    }
}
