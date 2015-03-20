package com.zemiak.movies.service.ui.admin;

import com.zemiak.movies.service.BatchLogService;
import com.zemiak.movies.domain.BatchLog;
import com.zemiak.movies.service.*;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named("batchLogEditForm")
public class BatchLogEditForm implements Serializable {
    @Inject private BatchLogService service;

    private Integer id;
    private BatchLog bean;

    public BatchLogEditForm() {
    }

    public String close() {
        return "index";
    }

    public BatchLog getBean() {
        return bean;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String check() {
        bean = service.find(id);

        if (null == bean) {
            JsfMessages.addErrorMessage("Cannot find batch log #" + id);
            return close();
        }

        return null;
    }
}
