"use client"

import { createContext, useState, useContext, ReactNode } from "react"

interface Song {
  id: number;
  title: string;
  artist: string;
  thumbnail: string;
  duration: string;
  isPlaying: boolean;
  videoId: string;
}


interface SongContextType {
  currentSong: Song | null
  setCurrentSong: (song: Song) => void
}

const SongContext = createContext<SongContextType>({
  currentSong: null,
  setCurrentSong: () => {},
})

export const SongProvider = ({ children }: { children: ReactNode }) => {
  const [currentSong, setCurrentSong] = useState<Song | null>(null)

  return (
    <SongContext.Provider value={{ currentSong, setCurrentSong }}>
      {children}
    </SongContext.Provider>
  )
}

export const useSong = () => useContext(SongContext)
