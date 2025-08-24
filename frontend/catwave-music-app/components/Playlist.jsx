import React, { useState, useEffect } from "react";
import axios from "axios";

export default function Playlist({ setCurrentSong }) {
  const [songs, setSongs] = useState([]);

  useEffect(() => {
    async function fetchSongs() {
      try {
        const res = await axios.get("http://localhost:1212/api/songs");
        console.log("Fetched songs:", res.data);
        setSongs(res.data);
      } catch (err) {
        console.error("Error fetching songs", err);
      }
    }
    fetchSongs();
  }, []);

  const handlePlay = (song) => {
    setCurrentSong({
      id: song.sid,
      title: song.audio_url, // or song.title if exists
      artist: "Unknown Artist", // bỏ qua author hiện tại
      thumbnail: "/placeholder.svg?height=60&width=60",
      duration: "3:45",
      isPlaying: true,
      src: `http://localhost:1212/api/audio/${song.sid}/stream`
    });
  };

  return (
    <ul>
      {songs.map((song, index) => (
        <li key={song.sid || index} onClick={() => handlePlay(song)}>
          {song.audio_url}
        </li>
      ))}
    </ul>
  );
}
