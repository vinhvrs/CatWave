"use client";

import { Play, Pause, Heart } from "lucide-react";
import { Button } from "@/components/ui/button";
import { usePlayer } from "@/lib/PlayerContext";
import type { Song } from "@/types/song";

interface RightSidebarProps {
  currentSong: Song | null;
}

export default function RightSidebar({ currentSong }: RightSidebarProps) {
  const { isPlaying, togglePlayPause } = usePlayer();

  return (
    <aside className="hidden xl:block w-80 p-6 space-y-6">
      <div className="flex items-center space-x-3">
        <img
          src={currentSong?.thumbnail || "/placeholder.svg"}
          alt={currentSong?.title || "No song selected"}
          className="w-16 h-16 rounded-lg"
        />
        <div className="flex-1 min-w-0">
          <h4 className="font-semibold truncate">{currentSong?.title || "No song"}</h4>
          <p className="text-gray-400 text-sm truncate">{currentSong?.artist || "Unknown Artist"}</p>
        </div>
      </div>

      <Button
        size="sm"
        className="gradient-primary border-0 rounded-full w-10 h-10"
        onClick={togglePlayPause}
      >
        {isPlaying ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4" />}
      </Button>
      <Button variant="ghost" size="sm">
        <Heart className="w-4 h-4" />
      </Button>
    </aside>
  );
}
