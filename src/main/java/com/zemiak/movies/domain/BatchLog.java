package com.zemiak.movies.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author vasko
 */
@Entity
@Table(name="batch_log", schema="data")
@NamedQueries({
    @NamedQuery(name = "BatchLog.findAll", query = "SELECT b FROM BatchLog b ORDER BY b.created DESC")
})
@SequenceGenerator(name="seq_batchlog", initialValue=21000, allocationSize=10)
public class BatchLog implements Serializable {
    @Transient
    static final private int SHORTENED_LENGTH = 64;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_batchlog")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Lob
    @NotNull
    private String text;
    
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date created;

    public BatchLog() {
        created = new Timestamp(new Date().getTime());
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }
    
    public String getShortenedText() {
        String text = this.text;
        if (text.length() > SHORTENED_LENGTH) {
            text = text.substring(0, SHORTENED_LENGTH) + " ...";
        }
        
        return text;
    }
    
    public String getFormattedCreated() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(created);
    }
}
