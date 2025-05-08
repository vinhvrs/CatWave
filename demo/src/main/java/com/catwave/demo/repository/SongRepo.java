package com.catwave.demo.repository;

import com.catwave.demo.model.Songs;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;



public interface SongRepo extends JpaRepository<Songs, UUID> {
    // Custom query methods can be defined here if needed
    // For example, to find songs by a specific category or artist
    // List<Songs> findByCategory(String category);
    // List<Songs> findByArtist(String artist);

}
