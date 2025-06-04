package com.catwave.demo.controller;

import com.catwave.demo.model.Member;
import com.catwave.demo.model.Playlist;
import com.catwave.demo.model.Songs;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.catwave.demo.repository.MemRepo;
import com.catwave.demo.repository.PlaySongRepo;
import com.catwave.demo.repository.PlaylistRepo;
import com.catwave.demo.repository.SongRepo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@CrossOrigin(origins = "*") // Allow all origins for testing
public class APIController {

    @Autowired
    private MemRepo memRepo;
    @Autowired
    private SongRepo songRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    APIController(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    @GetMapping("/api/members")
    public ResponseEntity<List<Member>> getMemeber() {
        List<Member> members = memRepo.findAll();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/api/member/getLocal")
    public ResponseEntity<Member> getMember(@RequestParam UUID uid) {
        Member member = memRepo.findByUID(uid);
        if (member == null) {
            return ResponseEntity.status(404).body(null);
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

    @DeleteMapping("/api/member/delete/{uid}")
    public ResponseEntity<String> deleteMember(@PathVariable UUID uid) {
        Member member = memRepo.findByUID(uid);
        if (member == null) {
            return ResponseEntity.status(404).body("Member not found");
        }
        memRepo.delete(member);
        return ResponseEntity.ok("Member deleted successfully");
    }

    @PutMapping("/api/member/update/{uid}")
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

    @Value("${youtube.api.key}")
    private String apiKey;

    @CrossOrigin(origins = {
        "http://localhost:1212",
        "https://2579-171-248-117-247.ngrok-free.app"
    })
    @GetMapping("/api/api_key")
    public ResponseEntity<String> getKey() {
        if (apiKey == null || apiKey.isEmpty()) {
            return ResponseEntity.status(404).body("API key not found");
        }
        return ResponseEntity.ok(apiKey);
    }

    
}
