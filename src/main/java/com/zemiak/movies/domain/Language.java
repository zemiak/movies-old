package com.zemiak.movies.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author vasko
 */
@Entity
@Table(name = "language", catalog = "movies", schema = "data")
@NamedQueries({
    @NamedQuery(name = "Language.findAll", query = "SELECT l FROM Language l"),
    @NamedQuery(name = "Language.findById", query = "SELECT l FROM Language l WHERE l.id = :id"),
    @NamedQuery(name = "Language.findByName", query = "SELECT l FROM Language l WHERE l.name = :name"),
    @NamedQuery(name = "Language.findByPictureFileName", query = "SELECT l FROM Language l WHERE l.pictureFileName = :pictureFileName"),
    @NamedQuery(name = "Language.findByDisplayOrder", query = "SELECT l FROM Language l WHERE l.displayOrder = :displayOrder")})
public class Language implements Serializable {
    private static final long serialVersionUID = 2L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "id")
    private String id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "name")
    private String name;
    
    @Size(max = 512)
    @Column(name = "picture_file_name")
    private String pictureFileName;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @OneToMany(mappedBy = "subtitles")
    private List<Movie> movieList;
    
    @OneToMany(mappedBy = "originalLanguage")
    private List<Movie> movieList1;
    
    @OneToMany(mappedBy = "language")
    private List<Movie> movieList2;

    public Language() {
    }

    public Language(String id) {
        this.id = id;
    }

    public Language(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> getMovieList1() {
        return movieList1;
    }

    public void setMovieList1(List<Movie> movieList1) {
        this.movieList1 = movieList1;
    }

    public List<Movie> getMovieList2() {
        return movieList2;
    }

    public void setMovieList2(List<Movie> movieList2) {
        this.movieList2 = movieList2;
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
        if (!(object instanceof Language)) {
            return false;
        }
        Language other = (Language) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zemiak.movies.domain.Language[ id=" + id + " ]";
    }
    
}
