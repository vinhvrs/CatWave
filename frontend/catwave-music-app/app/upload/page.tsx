"use client"

import type React from "react"

import { useState, useCallback } from "react"
import { ArrowLeft, Upload, Music, ImageIcon, X, Check } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import Link from "next/link"

export default function UploadPage() {
  const [dragActive, setDragActive] = useState(false)
  const [uploadedFile, setUploadedFile] = useState<File | null>(null)
  const [uploadProgress, setUploadProgress] = useState(0)
  const [isUploading, setIsUploading] = useState(false)
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    tags: "",
    genre: "",
    isPublic: true,
  })

  const handleDrag = useCallback((e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === "dragenter" || e.type === "dragover") {
      setDragActive(true)
    } else if (e.type === "dragleave") {
      setDragActive(false)
    }
  }, [])

  const handleDrop = useCallback((e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      const file = e.dataTransfer.files[0]
      if (file.type.startsWith("audio/")) {
        setUploadedFile(file)
      }
    }
  }, [])

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0]
      if (file.type.startsWith("audio/")) {
        setUploadedFile(file)
      }
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!uploadedFile) return

    setIsUploading(true)

    // Simulate upload progress
    for (let i = 0; i <= 100; i += 10) {
      setUploadProgress(i)
      await new Promise((resolve) => setTimeout(resolve, 200))
    }

    setIsUploading(false)
    // Handle successful upload
    console.log("Upload completed:", { file: uploadedFile, ...formData })
  }

  const removeFile = () => {
    setUploadedFile(null)
    setUploadProgress(0)
  }

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
        <div className="ml-auto">
          <h2 className="text-lg font-semibold">Upload Your Music</h2>
        </div>
      </header>

      <div className="container mx-auto px-6 py-8 max-w-4xl">
        <div className="grid lg:grid-cols-2 gap-8">
          {/* Upload Area */}
          <div className="space-y-6">
            <Card className="glass-effect border-gray-700">
              <CardHeader>
                <CardTitle className="flex items-center">
                  <Upload className="w-5 h-5 mr-2" />
                  Upload Audio File
                </CardTitle>
              </CardHeader>
              <CardContent>
                {!uploadedFile ? (
                  <div
                    className={`border-2 border-dashed rounded-xl p-8 text-center transition-colors ${
                      dragActive ? "border-purple-500 bg-purple-500/10" : "border-gray-600 hover:border-gray-500"
                    }`}
                    onDragEnter={handleDrag}
                    onDragLeave={handleDrag}
                    onDragOver={handleDrag}
                    onDrop={handleDrop}
                  >
                    <div className="w-16 h-16 mx-auto mb-4 gradient-primary rounded-full flex items-center justify-center">
                      <Music className="w-8 h-8" />
                    </div>
                    <h3 className="text-lg font-semibold mb-2">Drop your audio file here</h3>
                    <p className="text-gray-400 mb-4">or click to browse</p>
                    <input
                      type="file"
                      accept="audio/*"
                      onChange={handleFileChange}
                      className="hidden"
                      id="file-upload"
                    />
                    <Label htmlFor="file-upload">
                      <Button className="gradient-primary border-0" asChild>
                        <span>Choose File</span>
                      </Button>
                    </Label>
                    <p className="text-xs text-gray-500 mt-4">Supported formats: MP3, WAV, FLAC, AAC (Max 100MB)</p>
                  </div>
                ) : (
                  <div className="space-y-4">
                    <div className="flex items-center justify-between p-4 bg-gray-700/50 rounded-lg">
                      <div className="flex items-center space-x-3">
                        <div className="w-10 h-10 bg-purple-500 rounded-lg flex items-center justify-center">
                          <Music className="w-5 h-5" />
                        </div>
                        <div>
                          <p className="font-medium">{uploadedFile.name}</p>
                          <p className="text-sm text-gray-400">{(uploadedFile.size / (1024 * 1024)).toFixed(2)} MB</p>
                        </div>
                      </div>
                      <Button variant="ghost" size="sm" onClick={removeFile}>
                        <X className="w-4 h-4" />
                      </Button>
                    </div>

                    {isUploading && (
                      <div className="space-y-2">
                        <div className="flex justify-between text-sm">
                          <span>Uploading...</span>
                          <span>{uploadProgress}%</span>
                        </div>
                        <div className="w-full bg-gray-700 rounded-full h-2">
                          <div
                            className="gradient-primary h-2 rounded-full transition-all duration-300"
                            style={{ width: `${uploadProgress}%` }}
                          />
                        </div>
                      </div>
                    )}

                    {uploadProgress === 100 && !isUploading && (
                      <div className="flex items-center text-green-400 text-sm">
                        <Check className="w-4 h-4 mr-2" />
                        Upload completed successfully!
                      </div>
                    )}
                  </div>
                )}
              </CardContent>
            </Card>

            {/* Cover Art Upload */}
            <Card className="glass-effect border-gray-700">
              <CardHeader>
                <CardTitle className="flex items-center">
                  <ImageIcon className="w-5 h-5 mr-2" />
                  Cover Art (Optional)
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="border-2 border-dashed border-gray-600 rounded-lg p-6 text-center hover:border-gray-500 transition-colors">
                  <div className="w-12 h-12 mx-auto mb-3 bg-gray-700 rounded-lg flex items-center justify-center">
                    <ImageIcon className="w-6 h-6" />
                  </div>
                  <p className="text-sm text-gray-400 mb-3">Upload cover art</p>
                  <input type="file" accept="image/*" className="hidden" id="cover-upload" />
                  <Label htmlFor="cover-upload">
                    <Button variant="outline" size="sm" className="border-gray-600 bg-transparent" asChild>
                      <span>Choose Image</span>
                    </Button>
                  </Label>
                  <p className="text-xs text-gray-500 mt-2">JPG, PNG (Recommended: 1000x1000px)</p>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Track Details Form */}
          <div className="space-y-6">
            <Card className="glass-effect border-gray-700">
              <CardHeader>
                <CardTitle>Track Details</CardTitle>
              </CardHeader>
              <CardContent>
                <form onSubmit={handleSubmit} className="space-y-4">
                  <div className="space-y-2">
                    <Label htmlFor="title">Track Title *</Label>
                    <Input
                      id="title"
                      name="title"
                      placeholder="Enter track title"
                      value={formData.title}
                      onChange={handleInputChange}
                      className="bg-gray-700 border-gray-600"
                      required
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="genre">Genre</Label>
                    <Input
                      id="genre"
                      name="genre"
                      placeholder="e.g. Electronic, Hip Hop, Rock"
                      value={formData.genre}
                      onChange={handleInputChange}
                      className="bg-gray-700 border-gray-600"
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="description">Description</Label>
                    <Textarea
                      id="description"
                      name="description"
                      placeholder="Tell us about your track..."
                      value={formData.description}
                      onChange={handleInputChange}
                      className="bg-gray-700 border-gray-600 resize-none"
                      rows={4}
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="tags">Tags</Label>
                    <Input
                      id="tags"
                      name="tags"
                      placeholder="ambient, chill, electronic (comma separated)"
                      value={formData.tags}
                      onChange={handleInputChange}
                      className="bg-gray-700 border-gray-600"
                    />
                    <p className="text-xs text-gray-500">Add tags to help people discover your music</p>
                  </div>

                  <div className="flex items-center space-x-2">
                    <input
                      type="checkbox"
                      id="isPublic"
                      checked={formData.isPublic}
                      onChange={(e) => setFormData({ ...formData, isPublic: e.target.checked })}
                      className="rounded border-gray-600 bg-gray-700"
                    />
                    <Label htmlFor="isPublic" className="text-sm">
                      Make this track public
                    </Label>
                  </div>

                  <Button
                    type="submit"
                    className="w-full gradient-primary border-0"
                    disabled={!uploadedFile || isUploading}
                  >
                    {isUploading ? "Uploading..." : "Publish Track"}
                  </Button>
                </form>
              </CardContent>
            </Card>

            {/* Upload Guidelines */}
            <Card className="glass-effect border-gray-700">
              <CardHeader>
                <CardTitle className="text-lg">Upload Guidelines</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3 text-sm text-gray-300">
                <div className="flex items-start space-x-2">
                  <Check className="w-4 h-4 text-green-400 mt-0.5 flex-shrink-0" />
                  <span>Only upload music you own or have permission to share</span>
                </div>
                <div className="flex items-start space-x-2">
                  <Check className="w-4 h-4 text-green-400 mt-0.5 flex-shrink-0" />
                  <span>High-quality audio files get better engagement</span>
                </div>
                <div className="flex items-start space-x-2">
                  <Check className="w-4 h-4 text-green-400 mt-0.5 flex-shrink-0" />
                  <span>Add descriptive titles and tags for discoverability</span>
                </div>
                <div className="flex items-start space-x-2">
                  <Check className="w-4 h-4 text-green-400 mt-0.5 flex-shrink-0" />
                  <span>Cover art helps your track stand out</span>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}
