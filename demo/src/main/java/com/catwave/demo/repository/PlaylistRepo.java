package com.catwave.demo.repository;

import com.catwave.demo.model.Playlist;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface PlaylistRepo extends JpaRepository<Playlist, UUID>{
    // Create new playlist
    @Modifying
    @Transactional
    @Query(value = "Insert into Playlist (UID, PName) values (?1, ?2)", nativeQuery = true)
    void savePlaylist(UUID UID, String PName);

    // Get all playlists by UID
    @Query(value = "SELECT * FROM Playlist WHERE UID = ?1", nativeQuery = true)
    List<Playlist> findByUID(UUID UID);

    // Select playlist by PID
    @Query(value = "SELECT * FROM Playlist WHERE PID = ?1", nativeQuery = true)
    Playlist findByPID(UUID PID);

    // Select playlist by UID and PName
    @Query(value = "SELECT * FROM Playlist WHERE UID = ?1 AND PName = ?2", nativeQuery = true)
    Playlist findByUIDAndPName(UUID UID, String PName);

    // Delete playlist by PID
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Playlist WHERE PID = ?1", nativeQuery = true)
    void deleteByPID(UUID PID);

    // Update playlist name by PID
    @Modifying
    @Transactional
    @Query(value = "UPDATE Playlist SET PName = ?1 WHERE PID = ?2", nativeQuery = true)
    void updatePName(String PName, UUID PID);

}
