package com.catwave.demo.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "songs")
public class Songs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)", name = "sid", unique = true, nullable = false)
    private UUID SID;

    @Column(name = "AuID", unique = true, nullable = false)
    private UUID AuID;

    @Column(name = "AID", unique = true, nullable = false)
    private UUID AID;

    @Column(name = "audio_url", unique = false, nullable = false)
    private String audioUrl;

    @Column(name = "categories", unique = true, nullable = true)
    private String categories;

    @Column(name = "Lyrics", unique = false, nullable = true)
    private String lyrics;

    @Column(name = "Description", unique = false, nullable = true)
    private String description;

    @Column(name = "Hashtag", unique = false, nullable = true)
    private String hashtag;

    public UUID getSID() {
        return SID;
    }

    public void setSID(UUID SID) {
        this.SID = SID;
    }

    public UUID getAuID() {
        return AuID;
    }

    public void setAuID(UUID AuID) {
        this.AuID = AuID;
    }

    public UUID getAID() {
        return AID;
    }

    public void setAID(UUID AID) {
        this.AID = AID;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
    

}
