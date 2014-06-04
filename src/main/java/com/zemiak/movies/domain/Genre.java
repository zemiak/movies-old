package com.zemiak.movies.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
})
@XmlRootElement
public class Genre implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="my_gen", sequenceName="data.seq_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "name")
    private String name;

    @Column(name = "picture_file_name")
    @Size(max = 512)
    private String pictureFileName;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "genreId")
    private List<Serie> serieList;

    @OneToMany(mappedBy = "genreId")
    private List<Movie> movieList;
    
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Genre() {
        this.created = new Date();
    }

    public Genre(Integer id) {
        this();
        this.id = id;
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void copyFrom(Genre entity) {
        this.setId(entity.getId());
        this.setName(entity.getName());
        this.setDisplayOrder(entity.getDisplayOrder());
        this.setPictureFileName(entity.getPictureFileName());
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
    
    public boolean isEmpty() {
        return id == 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
