package com.catwave.demo.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PlaySong")
public class PlaySong {
    @Id
    @Column(columnDefinition = "BINARY(16)", name = "PID", unique = true, nullable = false)
    private UUID PID;

    @Id
    @Column(columnDefinition = "BINARY(16)", name = "SID", unique = true, nullable = false)
    private UUID SID;

    public UUID getPID() {
        return PID;
    }

    public void setPID(UUID PID) {
        this.PID = PID;
    }

    public UUID getSID() {
        return SID;
    }

    public void setSID(UUID SID) {
        this.SID = SID;
    }

}
