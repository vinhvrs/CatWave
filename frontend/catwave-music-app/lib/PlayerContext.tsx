"use client";

import { createContext, useState, useContext, useRef, ReactNode, MutableRefObject } from "react";
import type { YouTubePlayer } from "react-youtube";
import type { Song } from "@/types/song";

interface PlayerContextType {
  currentSong: Song | null;
  setCurrentSong: (song: Song) => void;
  playerRef: MutableRefObject<YouTubePlayer | null>;
  isPlaying: boolean;
  setIsPlaying: (playing: boolean) => void;
  play: () => void;
  pause: () => void;
  togglePlayPause: () => void;
}

interface PlayerProviderProps {
  children: ReactNode;
}

const PlayerContext = createContext<PlayerContextType>({
  currentSong: null,
  setCurrentSong: () => {},
  playerRef: { current: null },
  isPlaying: false,
  setIsPlaying: () => {},
  play: () => {},
  pause: () => {},
  togglePlayPause: () => {},
});

export const PlayerProvider = ({ children }: PlayerProviderProps) => {
  const [currentSong, setCurrentSong] = useState<Song | null>(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const playerRef = useRef<YouTubePlayer | null>(null);

  const play = () => {
    if (playerRef.current) {
      playerRef.current.playVideo();
      setIsPlaying(true);
    }
  };

  const pause = () => {
    if (playerRef.current) {
      playerRef.current.pauseVideo();
      setIsPlaying(false);
    }
  };

const togglePlayPause = () => {
  if (!playerRef.current) {
    console.log("Player not ready yet");
    return;
  }
  console.log("playerRef.current", playerRef.current);

  const state = playerRef.current.getPlayerState();
  console.log("Player state:", state);
  if (state === 1) { // playing
    pause();
  } else {
    play();
  }
};


  return (
    <PlayerContext.Provider value={{ currentSong, setCurrentSong, playerRef, isPlaying, setIsPlaying, play, pause, togglePlayPause }}>
      {children}
    </PlayerContext.Provider>
  );
};

export const usePlayer = () => {
  const context = useContext(PlayerContext);
  if (!context) {
    throw new Error("usePlayer must be used within a PlayerProvider");
  }
  return context;
};
