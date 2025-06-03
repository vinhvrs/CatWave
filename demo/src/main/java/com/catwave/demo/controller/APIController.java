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
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@CrossOrigin(origins = "*") // Allow all origins for testing
public class APIController {

    @Autowired
    private MemRepo memRepo;
    
    @Autowired
    private SongRepo songRepo;

    @Autowired
    private PlaylistRepo playlistRepo;

    @Autowired
    private PlaySongRepo playSongRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    APIController(SongRepo songRepo) {
        this.songRepo = songRepo;
    }

    @GetMapping("/api/members")
    public ResponseEntity<List<Member>> getMemeber() {
        List<Member> members = memRepo.findAll();
        return ResponseEntity.ok(members);
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

    @PutMapping("api/song/insert")
    public ResponseEntity<List<Songs>> songInsert(@RequestBody List<Songs> newSong) {
        for (Songs song : newSong) {
            Songs existingSong = songRepo.findBySID(song.getSID());
            if (existingSong != null) {
                continue;
            }
            songRepo.save(song);
        }

        return ResponseEntity.ok(newSong);
    }
    
    @PutMapping("api/song/{sid}/update")
    public String songUpdate(@PathVariable String id, @RequestBody Songs oldSong) {
    
        return "Song updated successfully";
    }

    // Create new playlist
    @PutMapping("api/{uid}/playlists/create")
    public ResponseEntity<Playlist> createPlaylist(@PathVariable UUID uid, @RequestBody Playlist newPlaylist) {
        newPlaylist.setUID(uid);
        playlistRepo.savePlaylist(uid, newPlaylist.getPName());
        return ResponseEntity.ok(newPlaylist);
    }

    // Get playlist by UID
    @GetMapping("api/{uid}/playlists")
    public ResponseEntity<List<Playlist>> getPlaylists(@PathVariable UUID uid) {
        List<Playlist> playlists = playlistRepo.findByUID(uid);
        if (playlists == null || playlists.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(playlists);
    }

    // Delete playlist by PID
    @DeleteMapping("api/playlists/{pid}/delete")
    public ResponseEntity<String> deletePlaylist(@PathVariable UUID pid) {
        Playlist playlist = playlistRepo.findByPID(pid);
        if (playlist == null) {
            return ResponseEntity.status(404).body("Playlist not found");
        }
        playlistRepo.deleteByPID(pid);
        playSongRepo.deleteByPID(pid);
        return ResponseEntity.ok("Playlist deleted successfully");
    }

    // Update playlist name by PID
    @PutMapping("api/playlists/{pid}/update")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable UUID pid, @RequestBody Playlist updatedPlaylist) {
        Playlist playlist = playlistRepo.findByPID(pid);
        if (playlist == null) {
            return ResponseEntity.status(404).body(null);
        }
        playlist.setPName(updatedPlaylist.getPName());
        playlistRepo.save(playlist);
        return ResponseEntity.ok(playlist);
    }

    // Search playlist by name
    @GetMapping("api/playlists/{uid}/search/{pname}")
    public ResponseEntity<Playlist> searchPlaylist(@PathVariable UUID uid, @PathVariable String pname) {
        Playlist playlist = playlistRepo.findByUIDAndPName(uid, pname);
        if (playlist == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(playlist);
    }

    // Add song to playlist by PID and SID
    @PutMapping("api/playlists/{pid}/songs/{sid}/add")
    public ResponseEntity<String> addSongToPlaylist(@PathVariable UUID pid, @PathVariable String sid) {
        Playlist playlist = playlistRepo.findByPID(pid);
        if (playlist == null) {
            return ResponseEntity.status(404).body("Playlist not found");
        }
        Songs song = songRepo.findBySID(sid);
        if (song == null) {
            return ResponseEntity.status(404).body("Song not found");
        }
        if (playSongRepo.findByPIDAndSID(pid, sid) != null) {
            return ResponseEntity.status(400).body("Song already exists in playlist");
        }
        playSongRepo.savePlaySong(pid, sid);
        return ResponseEntity.ok("Song added to playlist successfully");
    }

    // Get all songs in a playlist by PID
    @GetMapping("api/playlists/{pid}/songs")
    public ResponseEntity<List<Songs>> getSongsInPlaylist(@PathVariable UUID pid) {
        Playlist playlist = playlistRepo.findByPID(pid);
        if (playlist == null) {
            return ResponseEntity.status(404).body(null);
        }
        List<Songs> songs = songRepo.findAllByPlaylistId(pid);
        return ResponseEntity.ok(songs);
    }

    // Select song by SID in a playlist
    @GetMapping("api/playlists/{pid}/songs/{sid}")
    public ResponseEntity<Songs> getSongInPlaylist(@PathVariable UUID pid, @PathVariable String sid) {
        Playlist playlist = playlistRepo.findByPID(pid);
        if (playlist == null) {
            return ResponseEntity.status(404).body(null);
        }
        Songs song = songRepo.findBySID(sid); 
        if (song == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(song);
    }

    @DeleteMapping("api/playlists/{pid}/songs/{sid}/delete")
    public ResponseEntity<String> deleteSongFromPlaylist(@PathVariable UUID pid, @PathVariable String sid) {
        Playlist playlist = playlistRepo.findByPID(pid);
        if (playlist == null) {
            return ResponseEntity.status(404).body("Playlist not found");
        }
        Songs song = songRepo.findBySID(sid);
        if (song == null) {
            return ResponseEntity.status(404).body("Song not found");
        }
        playSongRepo.deleteByPIDAndSID(pid, sid);
        return ResponseEntity.ok("Song deleted from playlist successfully");
    }   

}
