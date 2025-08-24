"use client";

import { useState, useEffect } from "react";
import YouTube from "react-youtube";
import { Play, ChevronLeft, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { getPlaylists } from "@/lib/playlist";
import { usePlayer } from "@/lib/PlayerContext";
import type { Song } from "@/types/song";

interface MainContentProps {
  onSongSelect: (song: Song) => void;
  searchResults: Song[] | null;
  recommendedSongs: Song[];
}

export default function MainContent({
  searchResults,
  recommendedSongs,
}: MainContentProps) {
  const [currentSlide, setCurrentSlide] = useState(0);
  const [featuredPlaylists, setFeaturedPlaylists] = useState<any[]>([]);
  const [playingVideoId, setPlayingVideoId] = useState<string | null>(null);
  const { setCurrentSong } = usePlayer();

  
  // Fetch playlists
  useEffect(() => {
    const fetchPlaylists = async () => {
      const data = await getPlaylists("a5c17303-5b42-11f0-a19f-d8bbc178bee2");
      setFeaturedPlaylists(data);
    };
    fetchPlaylists();
  }, []);

  // Auto slide carousel
  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % featuredPlaylists.length);
    }, 5000);
    return () => clearInterval(timer);
  }, [featuredPlaylists.length]);

  const nextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % featuredPlaylists.length);
  };

  const prevSlide = () => {
    setCurrentSlide((prev) => (prev - 1 + featuredPlaylists.length) % featuredPlaylists.length);
  };

  const handlePlayPause = (song: Song) => {
    setCurrentSong(song);
    setPlayingVideoId((prev) => (prev === song.videoId ? null : song.videoId));
  };

  const opts = {
    height: "0",
    width: "0",
    playerVars: { autoplay: 1 },
  };

  return (
    <main className="flex-1 overflow-y-auto p-6 space-y-8">
      {searchResults ? (
        <section>
          <h2 className="text-2xl font-bold mb-6">Search Results</h2>
          {searchResults.length > 0 ? (
            <div className="space-y-2">
              {searchResults.map((song, index) => (
              <div
                key={song.id}
                className="flex items-center p-3 rounded-lg hover:bg-gray-800/50 transition-colors cursor-pointer group"
              >
                <span className="w-6 text-gray-400 text-sm">{index + 1}</span>
                <img
                  src={song.thumbnail || "/placeholder.svg"}
                  alt={song.title}
                  className="w-12 h-12 rounded-lg mx-4"
                />
                <div className="flex-1 min-w-0">
                  <h4 className="font-medium truncate">{song.title}</h4>
                  <p className="text-gray-400 text-sm truncate">{song.artist || "Unknown Artist"}</p>
                </div>
                <div className="flex items-center space-x-2">
                  <Button
                    size="sm"
                    onClick={() => setCurrentSong(song)} // 🔥 chỉ setCurrentSong
                    className="gradient-primary border-0 rounded-full w-8 h-8 opacity-0 group-hover:opacity-100 transition-opacity"
                  >
                    <Play className="w-3 h-3" />
                  </Button>
                </div>
              </div>
            ))}
            </div>
          ) : (
            <p className="text-gray-400">No results found.</p>
          )}
        </section>
      ) : (
        <>
          {/* Featured Playlists Carousel */}
          <section>
            <h2 className="text-2xl font-bold mb-6">Featured Playlists</h2>
            <div className="relative">
              <div className="overflow-hidden rounded-xl">
                <div
                  className="flex transition-transform duration-500 ease-in-out"
                  style={{ transform: `translateX(-${currentSlide * 100}%)` }}
                >
                  {featuredPlaylists.map((playlist) => (
                    <div key={playlist.pid} className="w-full flex-shrink-0">
                      <Card className="glass-effect border-gray-700 hover-lift cursor-pointer">
                        <CardContent className="p-0">
                          <div className="relative h-48 lg:h-64">
                            <img
                              src={playlist.thumbnailURL || "/placeholder.svg"}
                              alt={playlist.playlistName}
                              className="w-full h-full object-cover rounded-xl"
                            />
                            <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent rounded-xl" />
                            <div className="absolute bottom-4 left-4 right-4">
                              <h3 className="text-xl font-bold mb-1">{playlist.playlistName}</h3>
                              <p className="text-gray-300 text-sm mb-2">{playlist.description || "No description"}</p>
                              <p className="text-gray-400 text-xs">{playlist.songs?.length || 0} songs</p>
                            </div>
                            <Button
                              size="sm"
                              className="absolute top-4 right-4 gradient-primary border-0 rounded-full w-12 h-12"
                            >
                              <Play className="w-5 h-5" />
                            </Button>
                          </div>
                        </CardContent>
                      </Card>
                    </div>
                  ))}
                </div>
              </div>

              {/* Carousel Controls */}
              <Button
                variant="ghost"
                size="sm"
                className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-black/50 hover:bg-black/70 rounded-full"
                onClick={prevSlide}
              >
                <ChevronLeft className="w-5 h-5" />
              </Button>
              <Button
                variant="ghost"
                size="sm"
                className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-black/50 hover:bg-black/70 rounded-full"
                onClick={nextSlide}
              >
                <ChevronRight className="w-5 h-5" />
              </Button>
            </div>
          </section>

          {/* Recommended for You */}
          <section>
            <h2 className="text-2xl font-bold mb-6">Recommended for You</h2>
            <div className="flex space-x-4 overflow-x-auto pb-4">
              {recommendedSongs.map((song) => (
                <Card
                  key={song.id}
                  className="flex-shrink-0 w-48 glass-effect border-gray-700 hover-lift cursor-pointer group"
                  onClick={() => handlePlayPause(song)}
                >
                  <CardContent className="p-4">
                    <div className="relative mb-3">
                      <img
                        src={song.thumbnail || "/placeholder.svg"}
                        alt={song.title}
                        className="w-full h-32 object-cover rounded-lg"
                      />
                      <Button
                        size="sm"
                        className="absolute bottom-2 right-2 gradient-primary border-0 rounded-full w-10 h-10 opacity-0 group-hover:opacity-100 transition-opacity"
                      >
                        <Play className="w-4 h-4" />
                      </Button>
                    </div>
                    <h3 className="font-semibold truncate mb-1">{song.title}</h3>
                    <p className="text-gray-400 text-sm truncate">{song.artist || "Unknown Artist"}</p>
                  </CardContent>
                </Card>
              ))}
            </div>
          </section>
        </>
      )}
    </main>
  );
}
