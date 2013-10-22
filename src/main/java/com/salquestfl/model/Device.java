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
@Table(name="devices")
public class Device implements Serializable {
    @Id
    @SequenceGenerator(name="devices_id_gen", sequenceName="devices_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="devices_id_gen")
    private long id;
    @Column(nullable=false)
    private String mac_addr;
    private Timestamp manufactured_at;
    private Timestamp registered_at;
    @ManyToOne
    @JoinColumn(name="device_type_id", referencedColumnName="id")
    private DeviceType deviceType;
    @OneToMany(mappedBy="device")
    private Collection<Reading> readings;

    public Device() {
    }

    public Device(DeviceType deviceType, String mac_addr, Timestamp manufactured_at, Timestamp registered_at) {
        this.deviceType = deviceType;
        this.mac_addr = mac_addr;
        this.manufactured_at = manufactured_at;
        this.registered_at = registered_at;
    }

    public String getMac_addr() {
        return mac_addr;
    }

    public void setMac_addr(String mac_addr) {
        this.mac_addr = mac_addr;
    }

    public Timestamp getManufactured_at() {
        return manufactured_at;
    }

    public void setManufactured_at(Timestamp manufactured_at) {
        this.manufactured_at = manufactured_at;
    }

    public Timestamp getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at() {
        this.registered_at = registered_at;
    }

    @Override
    public int hashCode() {
        return mac_addr.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return mac_addr.equals(((Device) other).mac_addr);
    }
}
