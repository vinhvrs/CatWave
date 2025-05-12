package com.catwave.demo.repository;

import com.catwave.demo.model.Member;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemRepo extends JpaRepository<Member, UUID> {
    @Query("SELECT u FROM member WHERE u.username = ?1")
    Member findByUsername(String username);
    @Query("SELECT u FROM member WHERE u.UID = ?1")
    Member findByUID(UUID uid);
    @Query("SELECT u.UID FROM member WHERE u.username = ?1")
    UUID findUIDByUsername(String username);
    @Query("SELECT u FROM member WHERE u.email = ?1")
    Member findByEmail(String email);
}