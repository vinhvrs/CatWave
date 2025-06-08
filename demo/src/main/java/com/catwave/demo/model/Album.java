package com.catwave.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Album")
public class Album {
    @Id
    @Column(name = "AID", unique = true, nullable = false)
    private String AID;

    @Column(name = "SID", unique = false, nullable = true)
    private String SID;

    @Column(name = "AuID", unique = false, nullable = true)
    private String AuID;
    
    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getAuID() {
        return AuID;
    }

    public void setAuID(String AuID) {
        this.AuID = AuID;
    }
}
