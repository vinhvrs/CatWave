package com.catwave.demo.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Authors")
public class Authors {
    @Id
    @Column(name = "AuID", unique = true, nullable = false)
    private String AuID;

    @Column(name = "SID", unique = true, nullable = true)
    private String SID;

    @Column(name = "Description", unique = false, nullable = true)
    private String description;

    public String getAuID() {
        return AuID;
    }

    public void setAuID(String auID) {
        this.AuID = auID;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
