"use client";

import { useEffect } from "react";
import YouTube from "react-youtube";
import { usePlayer } from "@/lib/PlayerContext";

export default function BottomPlayer() {
  const { currentSong, playerRef, isPlaying, play, pause, setIsPlaying } = usePlayer();

  const onReady = (event: { target: any }) => {
    playerRef.current = event.target;
    setIsPlaying(true);
  };

  useEffect(() => {
    if (currentSong) {
      play();
    }
  }, [currentSong]);

  if (!currentSong) return null;

  return (
    <div className="fixed bottom-0 w-full bg-gray-900 p-4 flex items-center">
      <button onClick={isPlaying ? pause : play}>
        {isPlaying ? "Pause" : "Play"}
      </button>

      <YouTube
        videoId={currentSong.videoId}
        onReady={onReady}
        opts={{ height: "0", width: "0", playerVars: { autoplay: 1 } }}
      />
    </div>
  );
}
