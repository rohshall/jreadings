package com.salquestfl.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Collection;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;


@Entity
@Table(name="readings")
public class Reading {
    @Id
    @SequenceGenerator(name="readings_id_gen", sequenceName="readings_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="readings_id_gen")
    private long id;
    @Column(nullable=false)
    private String value;
    @Column(nullable=false)
    private Timestamp created_at;
    @ManyToOne
    @JoinColumn(name="device_mac_addr", referencedColumnName="mac_addr")
    private Device device;

    public Reading() {
    }

    public Reading(Device device, String value, Timestamp created_at) {
        this.device = device;
        this.value = value;
        this.created_at = created_at;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    
    @Override
    public int hashCode() {
        return new Long(id).intValue();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return id == ((Reading) other).id;
    }
}
