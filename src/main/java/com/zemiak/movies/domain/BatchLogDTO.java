package com.zemiak.movies.domain;

import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchLogDTO {
    private Integer id;
    private String text;
    private String created;

    public BatchLogDTO() {
    }

    public BatchLogDTO(BatchLog source) {
        id = source.getId();
        text = source.getText();
        created = null == source.getCreated() ? "" : new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(source.getCreated());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
