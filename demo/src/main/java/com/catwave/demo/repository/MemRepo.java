package com.catwave.demo.repository;

import com.catwave.demo.model.Member;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemRepo extends JpaRepository<Member, UUID> {
    @Query(value = "SELECT * FROM members WHERE username = ?1", nativeQuery = true)
    Member findByUsername(String username);
    @Query(value = "SELECT * FROM members WHERE UID = ?1", nativeQuery = true)
    Member findByUID(UUID uid);
    @Query(value = "SELECT UID FROM members WHERE username = ?1", nativeQuery = true)
    UUID findUIDByUsername(String username);
    @Query(value = "SELECT * FROM members WHERE email = ?1", nativeQuery = true)
    Member findByEmail(String email);
}