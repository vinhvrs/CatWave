"use client"

import type React from "react"

import { useState } from "react"
import { ArrowLeft, Play, Pause, Heart, Share, Plus, MessageCircle, MoreHorizontal } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Textarea } from "@/components/ui/textarea"
import Link from "next/link"

const songData = {
  id: 1,
  title: "Midnight Dreams",
  artist: "Luna Eclipse",
  thumbnail: "/placeholder.svg?height=300&width=300",
  duration: "3:45",
  tags: ["#electronic", "#ambient", "#chill", "#nighttime"],
  likes: 1247,
  plays: 15420,
  isLiked: false,
  uploadDate: "2 days ago",
}

const comments = [
  {
    id: 1,
    user: {
      name: "Alex Chen",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    content: "This track is absolutely mesmerizing! The ambient layers create such a dreamy atmosphere. 🌙✨",
    timestamp: "2 hours ago",
    likes: 12,
  },
  {
    id: 2,
    user: {
      name: "Sarah Johnson",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    content: "Perfect for late night coding sessions. The progression around 2:30 gives me chills every time!",
    timestamp: "5 hours ago",
    likes: 8,
  },
  {
    id: 3,
    user: {
      name: "Mike Rodriguez",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    content: "Luna Eclipse never disappoints. This is going straight to my favorites playlist! 🔥",
    timestamp: "1 day ago",
    likes: 15,
  },
]

export default function SongPage() {
  const [isPlaying, setIsPlaying] = useState(false)
  const [isLiked, setIsLiked] = useState(songData.isLiked)
  const [newComment, setNewComment] = useState("")
  const [currentTime, setCurrentTime] = useState(0)

  const togglePlay = () => {
    setIsPlaying(!isPlaying)
  }

  const toggleLike = () => {
    setIsLiked(!isLiked)
  }

  const handleCommentSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (newComment.trim()) {
      // Handle comment submission
      console.log("New comment:", newComment)
      setNewComment("")
    }
  }

  // Generate fake waveform data
  const waveformBars = Array.from({ length: 200 }, (_, i) => ({
    height: Math.random() * 100 + 10,
    played: i < (currentTime / 225) * 200, // 225 seconds = 3:45
  }))

  return (
    <div className="min-h-screen bg-gray-900 text-white">
      {/* Header */}
      <header className="h-16 bg-gray-800/50 backdrop-blur-md border-b border-gray-700/50 flex items-center px-6">
        <Link href="/">
          <Button variant="ghost" size="sm" className="mr-4">
            <ArrowLeft className="w-4 h-4 mr-2" />
            Back to Home
          </Button>
        </Link>
        <div className="flex items-center space-x-2">
          <div className="w-8 h-8 gradient-primary rounded-lg flex items-center justify-center">
            <svg viewBox="0 0 24 24" className="w-5 h-5 text-white">
              <path
                fill="currentColor"
                d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"
              />
            </svg>
          </div>
          <h1 className="text-xl font-bold bg-gradient-to-r from-purple-400 to-orange-400 bg-clip-text text-transparent">
            CatWave
          </h1>
        </div>
      </header>

      <div className="container mx-auto px-6 py-8">
        <div className="grid lg:grid-cols-3 gap-8">
          {/* Main Content */}
          <div className="lg:col-span-2 space-y-8">
            {/* Song Info */}
            <div className="flex flex-col md:flex-row gap-6">
              <img
                src={songData.thumbnail || "/placeholder.svg"}
                alt={songData.title}
                className="w-full md:w-64 h-64 object-cover rounded-xl"
              />
              <div className="flex-1 space-y-4">
                <div>
                  <h1 className="text-3xl md:text-4xl font-bold mb-2">{songData.title}</h1>
                  <p className="text-xl text-gray-300 mb-4">by {songData.artist}</p>
                  <div className="flex flex-wrap gap-2 mb-4">
                    {songData.tags.map((tag) => (
                      <span key={tag} className="px-3 py-1 bg-purple-500/20 text-purple-300 rounded-full text-sm">
                        {tag}
                      </span>
                    ))}
                  </div>
                </div>

                <div className="flex items-center space-x-6 text-sm text-gray-400">
                  <span>{songData.plays.toLocaleString()} plays</span>
                  <span>{songData.likes.toLocaleString()} likes</span>
                  <span>Posted {songData.uploadDate}</span>
                </div>

                <div className="flex items-center space-x-4">
                  <Button size="lg" className="gradient-primary border-0 rounded-full px-8" onClick={togglePlay}>
                    {isPlaying ? <Pause className="w-5 h-5 mr-2" /> : <Play className="w-5 h-5 mr-2" />}
                    {isPlaying ? "Pause" : "Play"}
                  </Button>

                  <Button
                    variant="outline"
                    size="lg"
                    className={`border-gray-600 ${isLiked ? "text-red-400 border-red-400" : ""}`}
                    onClick={toggleLike}
                  >
                    <Heart className={`w-4 h-4 mr-2 ${isLiked ? "fill-current" : ""}`} />
                    Like
                  </Button>

                  <Button variant="outline" size="lg" className="border-gray-600 bg-transparent">
                    <Share className="w-4 h-4 mr-2" />
                    Share
                  </Button>

                  <Button variant="outline" size="lg" className="border-gray-600 bg-transparent">
                    <Plus className="w-4 h-4 mr-2" />
                    Add to Playlist
                  </Button>
                </div>
              </div>
            </div>

            {/* Waveform */}
            <Card className="glass-effect border-gray-700">
              <CardContent className="p-6">
                <div className="relative">
                  <div className="flex items-end justify-center h-32 space-x-1 cursor-pointer">
                    {waveformBars.map((bar, index) => (
                      <div
                        key={index}
                        className={`w-1 transition-colors duration-200 ${
                          bar.played
                            ? "bg-gradient-to-t from-purple-500 to-orange-500"
                            : "bg-gray-600 hover:bg-gray-500"
                        }`}
                        style={{ height: `${bar.height}%` }}
                        onClick={() => setCurrentTime((index / 200) * 225)}
                      />
                    ))}
                  </div>

                  {/* Play button overlay */}
                  <Button
                    size="lg"
                    className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 gradient-primary border-0 rounded-full w-16 h-16"
                    onClick={togglePlay}
                  >
                    {isPlaying ? <Pause className="w-6 h-6" /> : <Play className="w-6 h-6" />}
                  </Button>
                </div>

                <div className="flex justify-between text-sm text-gray-400 mt-4">
                  <span>
                    {Math.floor(currentTime / 60)}:{(currentTime % 60).toString().padStart(2, "0")}
                  </span>
                  <span>{songData.duration}</span>
                </div>
              </CardContent>
            </Card>

            {/* Comments Section */}
            <Card className="glass-effect border-gray-700">
              <CardContent className="p-6">
                <h3 className="text-xl font-bold mb-6 flex items-center">
                  <MessageCircle className="w-5 h-5 mr-2" />
                  Comments ({comments.length})
                </h3>

                {/* Add Comment */}
                <form onSubmit={handleCommentSubmit} className="mb-6">
                  <div className="flex space-x-3">
                    <div className="w-10 h-10 bg-gradient-to-r from-purple-500 to-orange-500 rounded-full flex items-center justify-center flex-shrink-0">
                      <span className="text-sm font-bold">You</span>
                    </div>
                    <div className="flex-1">
                      <Textarea
                        placeholder="Add a comment..."
                        value={newComment}
                        onChange={(e) => setNewComment(e.target.value)}
                        className="bg-gray-700 border-gray-600 resize-none"
                        rows={3}
                      />
                      <div className="flex justify-end mt-2">
                        <Button
                          type="submit"
                          size="sm"
                          className="gradient-primary border-0"
                          disabled={!newComment.trim()}
                        >
                          Post Comment
                        </Button>
                      </div>
                    </div>
                  </div>
                </form>

                {/* Comments List */}
                <div className="space-y-6">
                  {comments.map((comment) => (
                    <div key={comment.id} className="flex space-x-3">
                      <img
                        src={comment.user.avatar || "/placeholder.svg"}
                        alt={comment.user.name}
                        className="w-10 h-10 rounded-full flex-shrink-0"
                      />
                      <div className="flex-1">
                        <div className="flex items-center space-x-2 mb-1">
                          <span className="font-medium">{comment.user.name}</span>
                          <span className="text-gray-400 text-sm">{comment.timestamp}</span>
                        </div>
                        <p className="text-gray-300 mb-2">{comment.content}</p>
                        <div className="flex items-center space-x-4 text-sm">
                          <Button variant="ghost" size="sm" className="text-gray-400 hover:text-white p-0">
                            <Heart className="w-3 h-3 mr-1" />
                            {comment.likes}
                          </Button>
                          <Button variant="ghost" size="sm" className="text-gray-400 hover:text-white p-0">
                            Reply
                          </Button>
                          <Button variant="ghost" size="sm" className="text-gray-400 hover:text-white p-0">
                            <MoreHorizontal className="w-3 h-3" />
                          </Button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Artist Info */}
            <Card className="glass-effect border-gray-700">
              <CardContent className="p-6">
                <div className="flex items-center space-x-4 mb-4">
                  <div className="w-16 h-16 bg-gradient-to-r from-purple-500 to-orange-500 rounded-full flex items-center justify-center">
                    <span className="text-lg font-bold">LE</span>
                  </div>
                  <div>
                    <h3 className="font-bold text-lg">{songData.artist}</h3>
                    <p className="text-gray-400 text-sm">Electronic Artist</p>
                  </div>
                </div>
                <Button className="w-full gradient-primary border-0">Follow Artist</Button>
              </CardContent>
            </Card>

            {/* More from Artist */}
            <Card className="glass-effect border-gray-700">
              <CardContent className="p-6">
                <h3 className="font-bold mb-4">More from {songData.artist}</h3>
                <div className="space-y-3">
                  {[
                    { title: "Stellar Voyage", plays: "8.2K" },
                    { title: "Cosmic Drift", plays: "12.1K" },
                    { title: "Aurora Nights", plays: "6.8K" },
                  ].map((track, index) => (
                    <div
                      key={index}
                      className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-700/50 cursor-pointer"
                    >
                      <div className="w-10 h-10 bg-gray-700 rounded-lg"></div>
                      <div className="flex-1">
                        <p className="font-medium text-sm">{track.title}</p>
                        <p className="text-gray-400 text-xs">{track.plays} plays</p>
                      </div>
                      <Button variant="ghost" size="sm">
                        <Play className="w-3 h-3" />
                      </Button>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}
