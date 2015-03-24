package com.zemiak.movies.service.ui.admin;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.service.GenreService;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

@SessionScoped
@Named("genreEditForm")
public class GenreEditForm implements Serializable {
    private static final int IMAGE_SIZE = 500*1024;

    private Integer id;
    private Genre bean;
    private Part file;

    @Inject
    private GenreService service;
    
    @Inject
    private String imgPath;

    public GenreEditForm() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String check() {
        bean = isNew() ? Genre.create() : service.find(id);

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find genre #" + id);
            return close();
        }

        return null;
    }

    public Genre getBean() {
        return bean;
    }

    public String save() {
        service.save(bean);
        JsfMessages.addSuccessMessage("The genre has been saved");

        return close();
    }

    public String close() {
        return "index";
    }

    public String remove() {
        try {
            service.remove(id);
            JsfMessages.addSuccessMessage("The genre has been deleted");
        } catch (EJBException ex) {
            JsfMessages.addErrorMessage(ex);
        }

        return close();
    }

    public String getActionTitle() {
        return isNew() ? "Create" : "Edit";
    }

    public boolean isNew() {
        return null == id || -1 == id;
    }

    public void upload() {
        try {
            Path imagePath = Paths.get(imgPath, "genre", bean.getPictureFileName());
            Files.copy(file.getInputStream(), imagePath);

            JsfMessages.addSuccessMessage("The image has been uploaded to " + imagePath.toString());
        } catch (IOException e) {
            JsfMessages.addErrorMessage(e);
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void validateFile(FacesContext ctx,
            UIComponent comp,
            Object value) {
        List<FacesMessage> msgs = new ArrayList<>();

        Part validatingFile = (Part) value;
        if (validatingFile.getSize() > IMAGE_SIZE) {
            msgs.add(new FacesMessage("File too big"));
        }

        if (!"image/jpeg".equals(validatingFile.getContentType().toLowerCase())) {
            msgs.add(new FacesMessage("Not a JPEG file"));
        }

        if (!msgs.isEmpty()) {
            throw new ValidatorException(msgs);
        }
    }
}
