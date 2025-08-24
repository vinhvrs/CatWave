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

    @PutMapping("/api/song/insert")
    public ResponseEntity<List<Songs>> songInsert(@RequestBody List<Songs> newSongs) {
        for (Songs song : newSongs) {
            Songs existingSong = songRepo.findBySID(song.getSID());
            if (existingSong != null) {
                continue; // skip if song already exists
            }

            // ✅ set default thumbnailUrl if null
            if (song.getThumbnailUrl() == null || song.getThumbnailUrl().isEmpty()) {
                song.setThumbnailUrl("https://via.placeholder.com/150"); // default placeholder
            }

            // ✅ set default viewCount if null
            if (song.getViewCount() == null) {
                song.setViewCount(0);
            }

            songRepo.save(song);
        }
        return ResponseEntity.ok(newSongs);
    }

    @PutMapping("/api/song/{sid}/updateViews")
    public ResponseEntity<String> updateSongViews(@PathVariable String sid) {
        Songs song = songRepo.findBySID(sid);
        if (song == null) {
            return ResponseEntity.status(404).body("Song not found");
        }
        Integer currentViews = song.getViewCount() != null ? song.getViewCount() : 0;
        song.setViewCount(currentViews + 1);
        songRepo.save(song);
        return ResponseEntity.ok("Song view count updated");
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


    @GetMapping("/api/songs")
    public ResponseEntity<List<Songs>> getAllSongs() {
        List<Songs> songs = songRepo.findAll();
        return ResponseEntity.ok(songs);
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