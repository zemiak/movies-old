package com.zemiak.movies.domain;

import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GenreDTO {
    private Integer id;
    private String name;
    private String pictureFileName;
    private Integer displayOrder;
    private String created;

    public GenreDTO() {
    }

    public GenreDTO(Genre source) {
        id = source.getId();
        name = source.getName();
        pictureFileName = source.getPictureFileName();
        displayOrder = source.getDisplayOrder();
        created = null == source.getCreated() ? "" : new SimpleDateFormat("yyyy-MM-dd").format(source.getCreated());
    }
}
