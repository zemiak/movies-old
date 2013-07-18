/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "serie", catalog = "movies", schema = "data")
@NamedQueries({
    @NamedQuery(name = "Serie.findAll", query = "SELECT s FROM Serie s"),
    @NamedQuery(name = "Serie.findById", query = "SELECT s FROM Serie s WHERE s.id = :id"),
    @NamedQuery(name = "Serie.findByName", query = "SELECT s FROM Serie s WHERE s.name = :name"),
    @NamedQuery(name = "Serie.findByPictureFileName", query = "SELECT s FROM Serie s WHERE s.pictureFileName = :pictureFileName"),
    @NamedQuery(name = "Serie.findByDisplayOrder", query = "SELECT s FROM Serie s WHERE s.displayOrder = :displayOrder"),
    @NamedQuery(name = "Serie.findByGenreId", query = "SELECT s FROM Serie s WHERE s.genreId = :genreId")})
public class Serie implements Serializable {
    private static final long serialVersionUID = 4L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 128)
    @Column(name = "name")
    private String name;
    
    @Size(max = 512)
    @Column(name = "picture_file_name")
    private String pictureFileName;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "genre_id")
    private Integer genreId;
    
    @OneToMany(mappedBy = "serieId")
    private List<Movie> movieList;

    public Serie() {
    }

    public Serie(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
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
        if (!(object instanceof Serie)) {
            return false;
        }
        Serie other = (Serie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zemiak.movies.domain.Serie[ id=" + id + " ]";
    }
    
}
