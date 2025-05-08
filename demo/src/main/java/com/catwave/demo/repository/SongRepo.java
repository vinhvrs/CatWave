package com.catwave.demo.repository;

import com.catwave.demo.model.Songs;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SongRepo extends JpaRepository<Songs, String> {
    // Custom query methods can be defined here if needed
    // For example, to find songs by a specific category or artist
    // List<Songs> findByCategory(String category);
    // List<Songs> findByArtist(String artist);
    @Query("SELECT u FROM Songs WHERE u.SID = ?1")
    Songs findBySID(String SID);
    
    // @Query("UPDATE Songs SET u.SID = ?1 WHERE u.SID = ?2")
    // Songs updateSID(String newSID, String oldSID);

}
