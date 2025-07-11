"use client"

import { Home, Compass, TrendingUp, Music, Heart, Plus, X } from "lucide-react"
import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"

interface SidebarProps {
  isOpen: boolean
  onToggle: () => void
}

const menuItems = [
  { icon: Home, label: "Home", active: true },
  { icon: Compass, label: "Explore" },
  { icon: TrendingUp, label: "Trending" },
  { icon: Music, label: "Your Library" },
]

const playlists = ["Liked Songs", "My Playlist #1", "Chill Vibes", "Workout Mix", "Study Focus"]

export default function Sidebar({ isOpen, onToggle }: SidebarProps) {
  return (
    <>
      {/* Mobile Overlay */}
      {isOpen && <div className="fixed inset-0 bg-black/50 z-40 lg:hidden" onClick={onToggle} />}

      {/* Sidebar */}
      <aside
        className={cn(
          "fixed lg:relative z-50 h-full w-64 bg-gray-800/50 backdrop-blur-md border-r border-gray-700/50 transition-transform duration-300",
          isOpen ? "translate-x-0" : "-translate-x-full lg:translate-x-0 lg:w-16",
        )}
      >
        <div className="p-4 h-full flex flex-col">
          {/* Close button for mobile */}
          <div className="flex justify-end lg:hidden mb-4">
            <Button variant="ghost" size="sm" onClick={onToggle}>
              <X className="w-4 h-4" />
            </Button>
          </div>

          {/* Main Navigation */}
          <nav className="space-y-2 mb-8">
            {menuItems.map((item) => (
              <Button
                key={item.label}
                variant="ghost"
                className={cn(
                  "w-full justify-start text-left hover:bg-gray-700/50",
                  item.active && "bg-gradient-to-r from-purple-500/20 to-orange-500/20 text-white",
                  !isOpen && "lg:justify-center lg:px-2",
                )}
              >
                <item.icon className={cn("w-5 h-5", isOpen && "mr-3")} />
                {isOpen && <span>{item.label}</span>}
              </Button>
            ))}
          </nav>

          {/* Playlists */}
          {isOpen && (
            <div className="flex-1">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-sm font-semibold text-gray-300 uppercase tracking-wider">Playlists</h3>
                <Button variant="ghost" size="sm">
                  <Plus className="w-4 h-4" />
                </Button>
              </div>

              <div className="space-y-1 overflow-y-auto">
                {playlists.map((playlist, index) => (
                  <Button
                    key={playlist}
                    variant="ghost"
                    className="w-full justify-start text-left text-gray-300 hover:text-white hover:bg-gray-700/50"
                  >
                    {index === 0 ? (
                      <Heart className="w-4 h-4 mr-3 text-purple-400" />
                    ) : (
                      <Music className="w-4 h-4 mr-3" />
                    )}
                    <span className="truncate">{playlist}</span>
                  </Button>
                ))}
              </div>
            </div>
          )}
        </div>
      </aside>
    </>
  )
}
