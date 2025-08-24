import { useState, useEffect } from "react";
import axios from "axios";

export default function useAudio() {
  const [songs, setSongs] = useState([]);
  const [currentSong, setCurrentSong] = useState(null);

  const fetchSongs = async () => {
    try {
      // ✅ Gọi API list songs (đảm bảo backend có endpoint này)
      const res = await axios.get("http://localhost:1212/api/audio/list");
      console.log("Fetched songs:", res.data);
      setSongs(res.data);
    } catch (err) {
      console.error("Error fetching songs", err);
    }
  };

  const playSong = (song) => {
    setCurrentSong({
      id: song.sid,
      title: song.title,
      artist: song.artist || "Unknown Artist",
      thumbnail: song.thumbnailUrl || "/placeholder.svg?height=60&width=60",
      duration: "3:45", // Nếu backend trả về duration thì lấy field đó thay thế
      isPlaying: true,
      src: `http://localhost:1212/api/audio/${song.sid}/stream`,
    });
  };

  useEffect(() => {
    fetchSongs();
  }, []);

  return { songs, currentSong, playSong };
}
