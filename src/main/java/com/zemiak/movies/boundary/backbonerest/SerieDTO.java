package com.zemiak.movies.boundary.backbonerest;

import com.zemiak.movies.domain.Serie;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vasko
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SerieDTO {
    private Integer id;
    private String name;
    private String pictureFileName;
    private Integer displayOrder;
    private Integer genreId;

    public SerieDTO() {
    }
    
    public SerieDTO(final Serie serie) {
        this();
        
        if (null == serie) {
            return;
        }
        
        this.id = serie.getId();
        this.name = serie.getName() == null ? "" : serie.getName();
        this.pictureFileName = serie.getPictureFileName() == null ? "" : serie.getPictureFileName();
        this.displayOrder = serie.getDisplayOrder() == null ? -1 : serie.getDisplayOrder();
        this.genreId = serie.getGenreId() == null ? null : serie.getGenreId().getId();
    }
}
