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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "serie", schema = "data")
@NamedQueries({
    @NamedQuery(name = "Serie.findAll", query = "SELECT s FROM Serie s ORDER BY s.genreId, s.displayOrder"),
    @NamedQuery(name = "Serie.findById", query = "SELECT s FROM Serie s WHERE s.id = :id"),
    @NamedQuery(name = "Serie.findByName", query = "SELECT s FROM Serie s WHERE s.name = :name"),
    @NamedQuery(name = "Serie.findByPictureFileName", query = "SELECT s FROM Serie s WHERE s.pictureFileName = :pictureFileName"),
    @NamedQuery(name = "Serie.findByDisplayOrder", query = "SELECT s FROM Serie s WHERE s.displayOrder = :displayOrder"),
    @NamedQuery(name = "Serie.findByGenreId", query = "SELECT s FROM Serie s WHERE s.genreId = :genreId")})
@XmlRootElement
public class Serie implements Serializable {
    private static final long serialVersionUID = 4L;
    
    public static final Integer MASH_ID = 1;
    public static final Integer GAME_OF_THRONES_ID = 1000;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="my_gen", sequenceName="seq_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_gen")
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

    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    @ManyToOne
    private Genre genreId;

    @OneToMany(mappedBy = "serieId")
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

    public Serie() {
        this.created = new Date();
    }

    public Serie(Integer id) {
        this();
        this.id = id;
    }

    public void copyFrom(Serie entity) {
        this.setId(entity.getId());
        this.setName(entity.getName());
        this.setDisplayOrder(entity.getDisplayOrder());
        this.setPictureFileName(entity.getPictureFileName());
        this.setGenreId(entity.getGenreId());
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

    public Genre getGenreId() {
        return genreId;
    }

    public void setGenreId(Genre genre) {
        this.genreId = genre;
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
        return getName();
    }

}
