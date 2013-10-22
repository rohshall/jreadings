package com.salquestfl.model;

import java.util.Collection;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.OneToMany;


@Entity
@Table(name="device_types")
public class DeviceType implements Serializable {
    @Id
    @SequenceGenerator(name="device_types_id_gen", sequenceName="device_types_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="device_types_id_gen")
    private long id;
    @Column(nullable=false)
    private String name;
    private String version;
    @OneToMany(mappedBy="deviceType")
    private Collection<Device> devices;

    public DeviceType() {
    }

    public DeviceType(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
