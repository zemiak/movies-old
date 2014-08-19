package com.zemiak.movies.service.backbonerest;

import com.zemiak.movies.domain.Genre;
import com.zemiak.movies.domain.Movie;
import com.zemiak.movies.strings.Encodings;
import java.util.Date;
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
    private String search;
    
    private String genreName;
    private String serieName;
    private String originalLanguageName;
    private String languageName;
    private String subtitlesName;
    
    private Date created;

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
        this.search = (null == this.name ? "" 
                    : Encodings.toAscii(this.name.trim().toLowerCase()));
        
        this.subtitlesName = movie.getSubtitles() == null ? "" : movie.getSubtitles().getName();
        this.originalLanguageName = movie.getOriginalLanguage() == null ? "" : movie.getOriginalLanguage().getName();
        this.languageName = movie.getLanguage() == null ? "" : movie.getLanguage().getName();
        this.genreName = movie.getGenreId() == null ? "" : movie.getGenreId().getName();
        this.serieName = movie.getSerieId() == null ? "" : movie.getSerieId().getName();
        
        this.created = movie.getCreated();
    }
    
    public Date getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getUrl() {
        return url;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSerieId() {
        return serieId;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public String getSearch() {
        return search;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getSerieName() {
        return serieName;
    }

    public String getOriginalLanguageName() {
        return originalLanguageName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getSubtitlesName() {
        return subtitlesName;
    }
    
    
}
