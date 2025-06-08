package com.catwave.demo.model;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PlaySongId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "BINARY(16)", name = "PID")
    private UUID PID;

    @Column(columnDefinition = "VARCHAR(255)",name = "SID")
    private String SID;

    public PlaySongId() {
    }

    public PlaySongId(UUID PID, String SID) {
        this.PID = PID;
        this.SID = SID;
    }

    // equals() & hashCode() based on PID & SID
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PlaySongId))
            return false;
        PlaySongId that = (PlaySongId) o;
        return PID.equals(that.PID) && SID.equals(that.SID);
    }

    @Override
    public int hashCode() {
        return PID.hashCode() * 31 + SID.hashCode();
    }

    public UUID getPID() {
        return PID;
    }

    public void setPID(UUID PID) {
        this.PID = PID;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }
}
