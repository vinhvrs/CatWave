package com.catwave.demo.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Authors")
public class Authors {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)", name = "AuID", unique = true, nullable = false)
    private UUID AuID;

    @Column(name = "Description", unique = false, nullable = true)
    private String description;

    public UUID getAuID() {
        return AuID;
    }

    public void setAuID(UUID auID) {
        this.AuID = auID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
