"use client"

import { useState, useRef, useEffect } from "react"
import YouTube from "react-youtube";
import type { YouTubePlayer } from "react-youtube";
import { Play, Pause, SkipBack, SkipForward, Shuffle, Repeat, Volume2, List, Monitor, Heart } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Slider } from "@/components/ui/slider"
import { useSong } from "@/lib/SongContext"
import type { Song } from "@/types/song";

interface MusicPlayerProps {
  currentSong: Song | null;
  onSongChange: (song: Song) => void;
}

export default function MusicPlayer({ currentSong, onSongChange }: MusicPlayerProps) {
  const [isPlaying, setIsPlaying] = useState(false)
  const [currentTime, setCurrentTime] = useState(0)
  const [volume, setVolume] = useState(75)
  const [isShuffled, setIsShuffled] = useState(false)
  const [repeatMode, setRepeatMode] = useState(0) // 0: off, 1: all, 2: one

  const playerRef = useRef<YouTubePlayer | null>(null)

  useEffect(() => {
    const interval = setInterval(() => {
      if (playerRef.current && isPlaying) {
        const time = playerRef.current.getCurrentTime()
        setCurrentTime(Math.floor(time))
      }
    }, 1000)

    return () => clearInterval(interval)
  }, [isPlaying])

  const onReady = (event: { target: YouTubePlayer }) => {
  playerRef.current = event.target
  event.target.setVolume(volume)
  event.target.playVideo()
  setIsPlaying(true)
}

  const togglePlay = () => {
  if (!playerRef.current) {
    console.warn("YouTube player not ready yet.")
    return
  }
  if (isPlaying) {
    playerRef.current.pauseVideo()
  } else {
    playerRef.current.playVideo()
  }
  setIsPlaying(!isPlaying)
}

    const handleEnded = () => {
      setIsPlaying(false)
      console.log("Song ended.")
    }

    const handleVolumeChange = (value: number[]) => {
    setVolume(value[0])
    if (playerRef.current) {
      playerRef.current.setVolume(value[0])
    }
  }

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60)
    const secs = seconds % 60
    return `${mins}:${secs.toString().padStart(2, "0")}`
  }

  if (!currentSong) return null

  return (
    <div className="h-20 bg-gray-800/90 backdrop-blur-md border-t border-gray-700/50 flex items-center px-4">
      {/* Song Info */}
      <div className="flex items-center space-x-3 w-80">
        <img
          src={currentSong.thumbnail || "/placeholder.svg"}
          alt={currentSong.title}
          className="w-14 h-14 rounded-lg"
        />
        <div className="min-w-0 flex-1">
          <h4 className="font-medium truncate text-sm">{currentSong?.title || "No song selected"}</h4>
          <p className="text-gray-400 text-xs truncate">{currentSong.artist}</p>
        </div>
        <Button variant="ghost" size="sm">
          <Heart className="w-4 h-4" />
        </Button>
      </div>

      {/* Player Controls */}
      <div className="flex-1 flex flex-col items-center space-y-2 max-w-2xl mx-8">
        <div className="flex items-center space-x-4">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setIsShuffled(!isShuffled)}
            className={isShuffled ? "text-purple-400" : "text-gray-400"}
          >
            <Shuffle className="w-4 h-4" />
          </Button>

          <Button variant="ghost" size="sm">
            <SkipBack className="w-5 h-5" />
          </Button>

          <Button size="sm" className="gradient-primary border-0 rounded-full w-10 h-10" onClick={togglePlay}>
            {isPlaying ? <Pause className="w-5 h-5" /> : <Play className="w-5 h-5" />}
          </Button>

          <Button variant="ghost" size="sm">
            <SkipForward className="w-5 h-5" />
          </Button>

          <Button
            variant="ghost"
            size="sm"
            onClick={() => setRepeatMode((repeatMode + 1) % 3)}
            className={repeatMode > 0 ? "text-purple-400" : "text-gray-400"}
          >
            <Repeat className="w-4 h-4" />
            {repeatMode === 2 && <span className="absolute -top-1 -right-1 w-2 h-2 bg-purple-400 rounded-full"></span>}
          </Button>
        </div>

        {/* Progress Bar */}
        <div className="flex items-center space-x-3 w-full">
          <span className="text-xs text-gray-400 w-10 text-right">{formatTime(currentTime)}</span>
          <Slider
            value={[currentTime]}
            onValueChange={(value) => {
              setCurrentTime(value[0])
              if (playerRef.current) {
                playerRef.current.seekTo(value[0], true)
              }
            }}
            max={225} // giả sử max 3:45
            step={1}
            className="flex-1"
          />
          <span className="text-xs text-gray-400 w-10">--:--</span> {/* duration YouTube không trả trực tiếp */}
        </div>
      </div>

      {/* Volume and Additional Controls */}
      <div className="flex items-center space-x-3 w-80 justify-end">
        <Button variant="ghost" size="sm">
          <List className="w-4 h-4" />
        </Button>

        <Button variant="ghost" size="sm">
          <Monitor className="w-4 h-4" />
        </Button>

        <div className="flex items-center space-x-2">
          <Volume2 className="w-4 h-4 text-gray-400" />
          <Slider
            value={[volume]}
            onValueChange={handleVolumeChange}
            max={100}
            step={1}
            className="w-20"
          />
        </div>
      </div>

      {/* Hidden YouTube Player */}
      <div className="hidden">
        <YouTube
          videoId={currentSong.videoId}
          onReady={onReady}
          onEnd={handleEnded}
          opts={{
            height: "0",
            width: "0",
            playerVars: {
              autoplay: 1,
            },
          }}
        />
      </div>
    </div>
  )
}
