package com.zemiak.movies.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="batch_log", schema="data")
@NamedQueries({
    @NamedQuery(name = "BatchLog.findAll", query = "SELECT b FROM BatchLog b ORDER BY b.created DESC")
})
public class BatchLog implements Serializable {
    @Transient
    static final private int SHORTENED_LENGTH = 64;

    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="data.entity_id_seq", allocationSize=1, initialValue = 30000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "text", columnDefinition = "TEXT")
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
