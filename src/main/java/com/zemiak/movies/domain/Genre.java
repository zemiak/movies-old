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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author vasko
 */
@Entity
@Table(name = "genre", schema = "data")
@NamedQueries({
    @NamedQuery(name = "Genre.findAll", query = "SELECT g FROM Genre g ORDER BY g.displayOrder"),
    @NamedQuery(name = "Genre.findById", query = "SELECT g FROM Genre g WHERE g.id = :id"),
    @NamedQuery(name = "Genre.findByName", query = "SELECT g FROM Genre g WHERE g.name = :name"),
    @NamedQuery(name = "Genre.findByPictureFileName", query = "SELECT g FROM Genre g WHERE g.pictureFileName = :pictureFileName"),
    @NamedQuery(name = "Genre.findByDisplayOrder", query = "SELECT g FROM Genre g WHERE g.displayOrder = :displayOrder"),
    @NamedQuery(name = "Genre.findByProtected1", query = "SELECT g FROM Genre g WHERE g.protected1 = :protected1")})
@XmlRootElement
public class Genre implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;

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

    @Basic(optional = false)
    @NotNull
    @Column(name = "protected")
    private int protected1;

    @OneToMany(mappedBy = "genreId")
    private List<Serie> serieList;

    @OneToMany(mappedBy = "genreId")
    private List<Movie> movieList;

    public Genre() {
    }

    public Genre(Integer id) {
        this.id = id;
    }

    public Genre(Integer id, String name, int protected1) {
        this.id = id;
        this.name = name;
        this.protected1 = protected1;
    }

    public void copyFrom(Genre entity) {
        this.setId(entity.getId());
        this.setName(entity.getName());
        this.setDisplayOrder(entity.getDisplayOrder());
        this.setPictureFileName(entity.getPictureFileName());
        this.setProtected1(entity.getProtected1());
    }

    public Integer getId() {
        return id;
    }

    @XmlTransient
    public List<Serie> getSerieList() {
        return serieList;
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

    public int getProtected1() {
        return protected1;
    }

    public void setProtected1(int protected1) {
        this.protected1 = protected1;
    }

    @XmlTransient
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
        if (!(object instanceof Genre)) {
            return false;
        }
        Genre other = (Genre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isEmpty() {
        return id == 0;
    }
}
