package com.zemiak.movies.boundary.backbonerest;

import com.zemiak.movies.domain.Movie;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vasko
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieDTO {
    private Integer id;
    private String fileName;
    private String name;
    private String originalName;
    private String url;
    private String pictureFileName;
    private Integer displayOrder;
    private String description;
    private Integer serieId;
    private String subtitles;
    private String originalLanguage;
    private String language;
    private Integer genreId;

    public MovieDTO() {
    }
    
    public MovieDTO(final Movie movie) {
        this();
        
        if (null == movie) {
            return;
        }
        
        this.id = movie.getId();
        this.fileName = movie.getFileName() == null ? "" : movie.getFileName();
        this.name = movie.getName() == null ? "" : movie.getName();
        this.originalName = movie.getOriginalName() == null ? "" : movie.getOriginalName();
        this.url = movie.getUrl() == null ? "" : movie.getUrl();
        this.pictureFileName = movie.getPictureFileName() == null ? "" : movie.getPictureFileName();
        this.displayOrder = movie.getDisplayOrder() == null ? -1 : movie.getDisplayOrder();
        this.description = movie.getDescription() == null ? "" : movie.getDescription();
        this.serieId = movie.getSerieId() == null ? null : movie.getSerieId().getId();
        this.subtitles = movie.getSubtitles() == null ? null : movie.getSubtitles().getId();
        this.originalLanguage = movie.getOriginalLanguage() == null ? null : movie.getOriginalLanguage().getId();
        this.language = movie.getLanguage() == null ? null : movie.getLanguage().getId();
        this.genreId = movie.getGenreId() == null ? null : movie.getGenreId().getId();
    }
}
