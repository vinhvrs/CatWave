package com.catwave.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catwave.demo.model.Playlist;
import com.catwave.demo.model.Songs;
import com.catwave.demo.repository.PlaySongRepo;
import com.catwave.demo.repository.PlaylistRepo;
import com.catwave.demo.repository.SongRepo;

@RestController
@CrossOrigin(origins = "*")
public class SongsController {
    @Autowired
    private SongRepo songRepo;
    @Autowired
    private PlaylistRepo playlistRepo;
    @Autowired
    private PlaySongRepo playSongRepo;

        // @GetMapping("api/song/{uid}")
    // public ResponseEntity<String> getSongByUID(@PathVariable UUID uid) {
    //     Member member = memRepo.findByUID(uid);
    //     if (member == null) {
    //         return ResponseEntity.status(404).body("Member not found");
    //     }
    //     return ResponseEntity.ok(member.getSong());
    // }

    @PutMapping("/api/song/insert")
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
    
    @PutMapping("/api/song/{sid}/update")
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
    @GetMapping("/api/{uid}/playlists")
    public ResponseEntity<List<Playlist>> getPlaylists(@PathVariable UUID uid) {
        List<Playlist> playlists = playlistRepo.findByUID(uid);
        if (playlists == null || playlists.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(playlists);
    }

    // Delete playlist by PID
    @DeleteMapping("/api/playlists/{pid}/delete")
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
    @PutMapping("/api/playlists/{pid}/update")
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
    @GetMapping("/api/playlists/{uid}/search/{pname}")
    public ResponseEntity<Playlist> searchPlaylist(@PathVariable UUID uid, @PathVariable String pname) {
        Playlist playlist = playlistRepo.findByUIDAndPName(uid, pname);
        if (playlist == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(playlist);
    }

    // Add song to playlist by PID and SID
    @PutMapping("/api/playlists/{pid}/songs/{sid}/add")
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
    @GetMapping("/api/playlists/{pid}/songs")
    public ResponseEntity<List<Songs>> getSongsInPlaylist(@PathVariable UUID pid) {
        Playlist playlist = playlistRepo.findByPID(pid);
        if (playlist == null) {
            return ResponseEntity.status(404).body(null);
        }
        List<Songs> songs = songRepo.findAllByPlaylistId(pid);
        return ResponseEntity.ok(songs);
    }

    // Select song by SID in a playlist
    @GetMapping("/api/playlists/{pid}/songs/{sid}")
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

    @DeleteMapping("/api/playlists/{pid}/songs/{sid}/delete")
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