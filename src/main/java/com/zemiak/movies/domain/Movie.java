package com.zemiak.movies.domain;

import com.zemiak.movies.description.Csfd;
import com.zemiak.movies.description.Imdb;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vasko
 */
@Entity
@Table(name = "movie", schema="data")
@NamedQueries({
    @NamedQuery(name = "Movie.findByGenreWithoutSerie", query = "SELECT m FROM Movie m WHERE m.genreId = :genre AND (m.serieId IS NULL OR m.serieId.id = 0) ORDER BY m.name"),
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m ORDER BY m.id"),
    @NamedQuery(name = "Movie.findById", query = "SELECT m FROM Movie m WHERE m.id = :id"),
    @NamedQuery(name = "Movie.findByIdDesc", query = "SELECT m FROM Movie m ORDER BY m.id DESC"),
    @NamedQuery(name = "Movie.findByFileName", query = "SELECT m FROM Movie m WHERE m.fileName = :fileName"),
    @NamedQuery(name = "Movie.findByName", query = "SELECT m FROM Movie m WHERE m.name = :name"),
    @NamedQuery(name = "Movie.findByOriginalName", query = "SELECT m FROM Movie m WHERE m.originalName = :originalName"),
    @NamedQuery(name = "Movie.findByUrl", query = "SELECT m FROM Movie m WHERE m.url = :url"),
    @NamedQuery(name = "Movie.findByPictureFileName", query = "SELECT m FROM Movie m WHERE m.pictureFileName = :pictureFileName"),
    @NamedQuery(name = "Movie.findByDisplayOrder", query = "SELECT m FROM Movie m WHERE m.displayOrder = :displayOrder"),
    @NamedQuery(name = "Movie.findByDescription", query = "SELECT m FROM Movie m WHERE m.description = :description")})
@XmlRootElement
@SequenceGenerator(name="seq_movie", initialValue=21000, allocationSize=10)
public class Movie implements Serializable {
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(generator = "seq_movie", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(max = 512)
    @Column(name = "file_name")
    private String fileName;

    @Size(max = 128)
    @Column(name = "name")
    private String name;

    @Size(max = 128)
    @Column(name = "original_name")
    private String originalName;

    @Size(max = 128)
    @Column(name = "url")
    private String url;

    @Size(max = 512)
    @Column(name = "picture_file_name")
    private String pictureFileName;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "serie_id", referencedColumnName = "id")
    @ManyToOne
    private Serie serieId;

    @JoinColumn(name = "subtitles", referencedColumnName = "id")
    @ManyToOne
    private Language subtitles;

    @JoinColumn(name = "original_language", referencedColumnName = "id")
    @ManyToOne
    private Language originalLanguage;

    @JoinColumn(name = "language", referencedColumnName = "id")
    @ManyToOne
    private Language language;

    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    @ManyToOne
    private Genre genreId;
    
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
    public Movie() {
        this.created = new Date();
    }

    public Movie(Integer id) {
        this();
        this.id = id;
    }

    public void copyFrom(Movie entity) {
        this.setId(entity.getId());
        this.setName(entity.getName());
        this.setDisplayOrder(entity.getDisplayOrder());
        this.setPictureFileName(entity.getPictureFileName());
        this.setDescription(entity.getDescription());
        this.setFileName(entity.getFileName());
        this.setGenreId(entity.getGenreId());
        this.setLanguage(entity.getLanguage());
        this.setOriginalLanguage(entity.getOriginalLanguage());
        this.setOriginalName(entity.getOriginalName());
        this.setSerieId(entity.getSerieId());
        this.setSubtitles(entity.getSubtitles());
        this.setUrl(entity.getUrl());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Serie getSerieId() {
        return serieId;
    }

    public void setSerieId(Serie serieId) {
        this.serieId = serieId;
    }

    public Language getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(Language subtitles) {
        this.subtitles = subtitles;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Genre getGenreId() {
        return genreId;
    }

    public void setGenreId(Genre genreId) {
        this.genreId = genreId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String composeGenreName() {
        if (null == getSerieId() || getSerieId().getId() == 0) {
            return getGenreId().getName();
        }

        return getGenreId().getDisplayOrder() + " " + getSerieId().getName();
    }

    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", name=" + name + '}';
    }

    public boolean isDescriptionEmpty() {
        return null == description || "".equals(description.trim()) || "''".equals(description.trim());
    }

    public boolean isUrlEmpty() {
        return null == url || "".equals(url.trim()) || "''".equals(url.trim());
    }

    public String getUrlFlag() {
        if (new Csfd().accepts(this)) {
            return "CSFD";
        } else if (new Imdb().accepts(this)) {
            return "IMDB";
        }

        return "";
    }
}
