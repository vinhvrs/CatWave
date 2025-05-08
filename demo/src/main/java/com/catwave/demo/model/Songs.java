package com.catwave.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "songs")
public class Songs {
    @Id
    @Column(name = "Sid", unique = true, nullable = false)
    private String SID;

    @Column(name = "AuID", unique = false, nullable = true)
    private String AuID;

    @Column(name = "AID", unique = false, nullable = true)
    private String AID;

    @Column(name = "audio_url", unique = false, nullable = true)
    private String audioUrl;

    @Column(name = "categories", unique = false, nullable = true)
    private String categories;

    @Column(name = "Lyrics", unique = false, nullable = true)
    private String lyrics;

    @Column(name = "Description", unique = false, nullable = true)
    private String description;

    @Column(name = "Hashtag", unique = false, nullable = true)
    private String hashtag;

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

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
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
