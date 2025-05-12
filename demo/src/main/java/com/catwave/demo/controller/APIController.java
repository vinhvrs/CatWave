package com.catwave.demo.controller;

import com.catwave.demo.model.Member;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;


import com.catwave.demo.repository.MemRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class APIController {
    @Autowired
    private MemRepo memRepo;

    @GetMapping("/api/members")
    public ResponseEntity<List<Member>> getMemeber() {
        List<Member> members = memRepo.findAll();
        return ResponseEntity.ok(members);
    }

    @PostMapping("/api/registation")
    public ResponseEntity<String> memberRegister(@RequestBody Member member) {
        if (member.getUsername() == null || member.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        String username = member.getUsername();
        if (memRepo.findByUsername(username) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        memRepo.save(member);
        return ResponseEntity.ok("Member registered successfully");

    }

    @GetMapping("/api/login")
    public ResponseEntity<Member> memberLogin(@RequestBody Member loginMember) {
        Member member = memRepo.findByUsername(loginMember.getUsername());
        String password = loginMember.getPassword();
        if (member == null) {
            return ResponseEntity.status(404).body(null);
        }
        if (!member.getPassword().equals(password)) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(member);
    }

    @GetMapping("/api/searching/{username}")
    public ResponseEntity<Member> getMemberByName(@PathVariable String username) {
    Member member = memRepo.findByUsername(username);
        if (member == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(member);
    }

    @GetMapping("/api/member/{uid}")
    public ResponseEntity<Member> getMemberByUID(@PathVariable UUID uid) {
        Member member = memRepo.findByUID(uid);
        if (member == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(member);
    }

    @GetMapping("/api/member/delete/{uid}")
    public ResponseEntity<String> deleteMember(@PathVariable UUID uid) {
        Member member = memRepo.findByUID(uid);
        if (member == null) {
            return ResponseEntity.status(404).body("Member not found");
        }
        memRepo.delete(member);
        return ResponseEntity.ok("Member deleted successfully");
    }

    @GetMapping("/api/member/update/{uid}")
    public ResponseEntity<String> updateMember(@PathVariable UUID uid, @RequestBody Member updatedMember) {
        Member member = memRepo.findByUID(uid);
        if (member == null) {
            return ResponseEntity.status(404).body("Member not found");
        }
        member.setUsername(updatedMember.getUsername());
        member.setPassword(updatedMember.getPassword());
        member.setEmail(updatedMember.getEmail());
        memRepo.save(member);
        return ResponseEntity.ok("Member updated successfully");
    }

    // @GetMapping("api/song/{uid}")
    // public ResponseEntity<String> getSongByUID(@PathVariable UUID uid) {
    //     Member member = memRepo.findByUID(uid);
    //     if (member == null) {
    //         return ResponseEntity.status(404).body("Member not found");
    //     }
    //     return ResponseEntity.ok(member.getSong());
    // }

    @Value("${youtube.api.key}")
    private String apiKey;

    @GetMapping("/api/api_key")
    public String getKey() {
        return apiKey;
    }
    

}
