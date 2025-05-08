package com.catwave.demo.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)", name = "AID", unique = true, nullable = false)
    private UUID AID;

    @Column(name = "AuID", unique = true, nullable = false)
    private UUID AuID;
    
    public UUID getAID() {
        return AID;
    }

    public void setAID(UUID AID) {
        this.AID = AID;
    }

    public UUID getAuID() {
        return AuID;
    }

    public void setAuID(UUID AuID) {
        this.AuID = AuID;
    }
}
