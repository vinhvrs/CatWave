"use client"

import { useState } from "react"
import { ArrowLeft, Bell, Play, Volume2, Shield, User, Palette } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Switch } from "@/components/ui/switch"
import { Label } from "@/components/ui/label"
import { Slider } from "@/components/ui/slider"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import Link from "next/link"

export default function SettingsPage() {
  const [settings, setSettings] = useState({
    darkMode: true,
    notifications: true,
    autoplay: true,
    highQuality: false,
    crossfade: true,
    volume: 75,
    language: "en",
    theme: "purple-orange",
  })

  const updateSetting = (key: string, value: any) => {
    setSettings((prev) => ({ ...prev, [key]: value }))
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
          <h2 className="text-lg font-semibold">Settings</h2>
        </div>
      </header>

      <div className="container mx-auto px-6 py-8 max-w-4xl">
        <div className="grid gap-6">
          {/* Appearance Settings */}
          <Card className="glass-effect border-gray-700">
            <CardHeader>
              <CardTitle className="flex items-center">
                <Palette className="w-5 h-5 mr-2" />
                Appearance
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Dark Mode</Label>
                  <p className="text-sm text-gray-400">Use dark theme across the application</p>
                </div>
                <Switch checked={settings.darkMode} onCheckedChange={(checked) => updateSetting("darkMode", checked)} />
              </div>

              <div className="space-y-3">
                <Label className="text-base font-medium">Theme Color</Label>
                <Select value={settings.theme} onValueChange={(value) => updateSetting("theme", value)}>
                  <SelectTrigger className="bg-gray-700 border-gray-600">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent className="bg-gray-800 border-gray-700">
                    <SelectItem value="purple-orange">Purple & Orange (Default)</SelectItem>
                    <SelectItem value="blue-cyan">Blue & Cyan</SelectItem>
                    <SelectItem value="green-lime">Green & Lime</SelectItem>
                    <SelectItem value="pink-rose">Pink & Rose</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-3">
                <Label className="text-base font-medium">Language</Label>
                <Select value={settings.language} onValueChange={(value) => updateSetting("language", value)}>
                  <SelectTrigger className="bg-gray-700 border-gray-600">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent className="bg-gray-800 border-gray-700">
                    <SelectItem value="en">English</SelectItem>
                    <SelectItem value="es">Español</SelectItem>
                    <SelectItem value="fr">Français</SelectItem>
                    <SelectItem value="de">Deutsch</SelectItem>
                    <SelectItem value="ja">日本語</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardContent>
          </Card>

          {/* Audio Settings */}
          <Card className="glass-effect border-gray-700">
            <CardHeader>
              <CardTitle className="flex items-center">
                <Volume2 className="w-5 h-5 mr-2" />
                Audio
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Autoplay</Label>
                  <p className="text-sm text-gray-400">Automatically play next track when current ends</p>
                </div>
                <Switch checked={settings.autoplay} onCheckedChange={(checked) => updateSetting("autoplay", checked)} />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">High Quality Audio</Label>
                  <p className="text-sm text-gray-400">Stream at higher bitrates (uses more data)</p>
                </div>
                <Switch
                  checked={settings.highQuality}
                  onCheckedChange={(checked) => updateSetting("highQuality", checked)}
                />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Crossfade</Label>
                  <p className="text-sm text-gray-400">Smooth transition between tracks</p>
                </div>
                <Switch
                  checked={settings.crossfade}
                  onCheckedChange={(checked) => updateSetting("crossfade", checked)}
                />
              </div>

              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <Label className="text-base font-medium">Default Volume</Label>
                  <span className="text-sm text-gray-400">{settings.volume}%</span>
                </div>
                <Slider
                  value={[settings.volume]}
                  onValueChange={(value) => updateSetting("volume", value[0])}
                  max={100}
                  step={1}
                  className="w-full"
                />
              </div>
            </CardContent>
          </Card>

          {/* Playback Settings */}
          <Card className="glass-effect border-gray-700">
            <CardHeader>
              <CardTitle className="flex items-center">
                <Play className="w-5 h-5 mr-2" />
                Playback
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Show Explicit Content</Label>
                  <p className="text-sm text-gray-400">Allow explicit tracks in recommendations</p>
                </div>
                <Switch defaultChecked />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Normalize Volume</Label>
                  <p className="text-sm text-gray-400">Keep consistent volume across all tracks</p>
                </div>
                <Switch defaultChecked />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Gapless Playback</Label>
                  <p className="text-sm text-gray-400">Remove silence between tracks</p>
                </div>
                <Switch />
              </div>
            </CardContent>
          </Card>

          {/* Notification Settings */}
          <Card className="glass-effect border-gray-700">
            <CardHeader>
              <CardTitle className="flex items-center">
                <Bell className="w-5 h-5 mr-2" />
                Notifications
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Push Notifications</Label>
                  <p className="text-sm text-gray-400">Receive notifications about new releases and updates</p>
                </div>
                <Switch
                  checked={settings.notifications}
                  onCheckedChange={(checked) => updateSetting("notifications", checked)}
                />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">New Releases</Label>
                  <p className="text-sm text-gray-400">Get notified when artists you follow release new music</p>
                </div>
                <Switch defaultChecked />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Playlist Updates</Label>
                  <p className="text-sm text-gray-400">Notifications when followed playlists are updated</p>
                </div>
                <Switch />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Email Notifications</Label>
                  <p className="text-sm text-gray-400">Receive weekly music recommendations via email</p>
                </div>
                <Switch />
              </div>
            </CardContent>
          </Card>

          {/* Privacy & Security */}
          <Card className="glass-effect border-gray-700">
            <CardHeader>
              <CardTitle className="flex items-center">
                <Shield className="w-5 h-5 mr-2" />
                Privacy & Security
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Private Session</Label>
                  <p className="text-sm text-gray-400">Don't save listening history during this session</p>
                </div>
                <Switch />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Show Activity</Label>
                  <p className="text-sm text-gray-400">Let friends see what you're listening to</p>
                </div>
                <Switch defaultChecked />
              </div>

              <div className="flex items-center justify-between">
                <div className="space-y-1">
                  <Label className="text-base font-medium">Data Usage Analytics</Label>
                  <p className="text-sm text-gray-400">Help improve CatWave by sharing usage data</p>
                </div>
                <Switch defaultChecked />
              </div>

              <div className="pt-4 border-t border-gray-700">
                <div className="space-y-3">
                  <Button variant="outline" className="w-full border-gray-600 bg-transparent">
                    Download My Data
                  </Button>
                  <Button
                    variant="outline"
                    className="w-full border-red-600 text-red-400 hover:bg-red-600/10 bg-transparent"
                  >
                    Delete Account
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Account Settings */}
          <Card className="glass-effect border-gray-700">
            <CardHeader>
              <CardTitle className="flex items-center">
                <User className="w-5 h-5 mr-2" />
                Account
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <Button variant="outline" className="border-gray-600 bg-transparent">
                  Change Password
                </Button>
                <Button variant="outline" className="border-gray-600 bg-transparent">
                  Update Email
                </Button>
                <Button variant="outline" className="border-gray-600 bg-transparent">
                  Manage Subscription
                </Button>
                <Button variant="outline" className="border-gray-600 bg-transparent">
                  Connected Apps
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Save Settings */}
          <div className="flex justify-end space-x-4">
            <Button variant="outline" className="border-gray-600 bg-transparent">
              Reset to Defaults
            </Button>
            <Button className="gradient-primary border-0">Save Changes</Button>
          </div>
        </div>
      </div>
    </div>
  )
}
