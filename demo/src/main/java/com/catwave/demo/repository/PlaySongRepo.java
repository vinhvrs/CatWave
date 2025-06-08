package com.catwave.demo.repository;
import com.catwave.demo.model.PlaySong;
import com.catwave.demo.model.PlaySongId;
import com.catwave.demo.model.Songs;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface PlaySongRepo extends JpaRepository<PlaySong, PlaySongId> {
    // Remove Songs by PID and SID
    @Query("DELETE FROM PlaySong WHERE PID = ?1 AND SID = ?2")
    void deleteByPIDAndSID(UUID PID, String SID);

    // Add Songs by PID and SID
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PlaySong (PID, SID) VALUES (?1, ?2)" ,nativeQuery = true)
    void savePlaySong(UUID PID, String SID);

    // Delete all Songs by PID
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PlaySong WHERE PID = ?1", nativeQuery = true)
    void deleteByPID(UUID PID);

    // Get all Songs by PID
    @Query(value = "SELECT * FROM PlaySong WHERE PID = ?1 AND SID = ?2", nativeQuery = true)
    PlaySong findByPIDAndSID(UUID PID, String SID);

}
