package com.catwave.demo.controller;

import com.catwave.demo.model.Member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.catwave.demo.repository.MemRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class APIController {
    @Autowired
    private MemRepo memRepo;

    @GetMapping("/api/getMember")
    public ResponseEntity<List<Member>> getMemeber() {
        List<Member> members = memRepo.findAll();
        return ResponseEntity.ok(members);
    }

    




}
