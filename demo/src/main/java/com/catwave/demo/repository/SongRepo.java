package com.catwave.demo.repository;

import com.catwave.demo.model.Songs;

import java.beans.Transient;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;


public interface SongRepo extends JpaRepository<Songs, String> {
    @Query(value = "SELECT * FROM Songs WHERE SID = ?1", nativeQuery = true)
    Songs findBySID(String SID);

    @Modifying
    @Transient
    @Query(value = "SELECT s.* FROM songs s JOIN playsong p ON s.sid = p.sid WHERE p.pid = ?1", nativeQuery = true)
    List<Songs> findAllByPlaylistId(UUID PID);
    // @Query("UPDATE Songs SET u.SID = ?1 WHERE u.SID = ?2")
    // Songs updateSID(String newSID, String oldSID);

}
