package com.catwave.demo.model;

import java.beans.Transient;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "Playsong")
@IdClass(PlaySongId.class)
public class PlaySong {
    @Id
    @Column(columnDefinition = "BINARY(16)", name = "PID", unique = true, nullable = false)
    private UUID PID;

    @Id
    @Column(name = "SID", unique = true, nullable = false)
    private String SID;

    public UUID getPID() {
        return PID;
    }

    public void setPID(UUID PID) {
        this.PID = PID;
    }

    @Transient
    public String getSID() {
        return SID;
    }
    
    @Transient
    public void setSID(String SID) {
        this.SID = SID;
    }

}
