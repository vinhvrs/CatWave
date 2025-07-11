"use client"

import { useState, useEffect } from "react"
import Header from "@/components/Header"
import Sidebar from "@/components/Sidebar"
import MainContent from "@/components/MainContent"
import RightSidebar from "@/components/RightSidebar"
import MusicPlayer from "@/components/MusicPlayer"
import LoginModal from "@/components/LoginModal"
import { SearchContext } from "@/lib/SearchContext"
import { getAllSongs, searchYouTube } from "@/lib/song"
import { SongProvider } from "@/lib/SongContext"
import type { Song } from "@/types/song";
import { usePlayer } from "@/lib/PlayerContext";

export default function Home() {
  const [sidebarOpen, setSidebarOpen] = useState(true)
  const [loginModalOpen, setLoginModalOpen] = useState(false)
  const { currentSong, setCurrentSong } = usePlayer();

  const [recommendedSongs, setRecommendedSongs] = useState<any[]>([])
  const [searchResults, setSearchResults] = useState<any[] | null>(null)

  // Fetch all songs
  useEffect(() => {
    const fetchSongs = async () => {
      const data = await getAllSongs()
      console.log("Fetched songs from DB:", data)
      setRecommendedSongs(data)
    }
    fetchSongs()
  }, [])

  // YouTube search
  const handleSearch = async (keyword: string) => {
    console.log("Searching YouTube for:", keyword);
    const results = await searchYouTube(keyword);
    console.log("YouTube search results:", results);
    setSearchResults(results);
  };

  return (
    <SearchContext.Provider value={{ handleSearch }}>
      <SongProvider>
        <div className="h-screen flex flex-col bg-gray-900">
          <Header onLoginClick={() => setLoginModalOpen(true)} />

          <div className="flex flex-1 overflow-hidden">
            <Sidebar isOpen={sidebarOpen} onToggle={() => setSidebarOpen(!sidebarOpen)} />

            <div className="flex-1 flex overflow-hidden">
              <MainContent
                onSongSelect={(song: Song) => setCurrentSong(song)}
                searchResults={searchResults}
                recommendedSongs={recommendedSongs}
              />
              <RightSidebar currentSong={currentSong} />
            </div>
          </div>

          <MusicPlayer currentSong={currentSong} onSongChange={setCurrentSong} />

          <LoginModal isOpen={loginModalOpen} onClose={() => setLoginModalOpen(false)} />
        </div>
      </SongProvider>
    </SearchContext.Provider>
  )
}
