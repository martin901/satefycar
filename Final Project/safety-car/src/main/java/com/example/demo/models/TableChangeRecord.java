package com.example.demo.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "table_change_records")
public class TableChangeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfChange;

    @ManyToOne
    private User user;

    public TableChangeRecord() {
        this.dateOfChange = new Date();
    }

    public TableChangeRecord(Date dateOfChange) {
        this.dateOfChange = dateOfChange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfChange() {
        return dateOfChange;
    }

    public void setDateOfChange(Date dateOfChange) {
        this.dateOfChange = dateOfChange;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
