package com.catwave.demo.repository;

import com.catwave.demo.model.Member;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemRepo extends JpaRepository<Member, UUID> {
    // This interface will automatically provide CRUD operations for the Mem entity
    // using Spring Data JPA.
    // No need to implement any methods here, as Spring Data JPA will generate the
    // implementation at runtime.
    
}