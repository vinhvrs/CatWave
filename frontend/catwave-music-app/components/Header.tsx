"use client"

import { useState } from "react"
import { Search, Upload, Bell, User } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { useAuth } from "@/lib/AuthContext";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { useSearch } from "@/lib/SearchContext"

interface HeaderProps {
  onLoginClick: () => void
}

export default function Header({ onLoginClick }: HeaderProps) {
  const [searchQuery, setSearchQuery] = useState("")
  const { handleSearch } = useSearch()
  const { user, logout } = useAuth();
  console.log("Header user: ", user);
  return (
    <header className="h-16 bg-gray-800/50 backdrop-blur-md border-b border-gray-700/50 flex items-center px-4 lg:px-6 z-50">
      {/* Logo */}
      <div className="flex items-center space-x-2 mr-8">
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

      {/* Search Bar */}
      <div className="flex-1 max-w-2xl mx-4">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
          <Input
            type="text"
            placeholder="Search songs, artists..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                handleSearch(searchQuery.trim());
              }
            }}
            className="w-full pl-10 pr-4 py-2 bg-gray-700/50 border-gray-600 rounded-full focus:ring-2 focus:ring-purple-500 focus:border-transparent"
          />
        </div>
      </div>

      {/* Right Side Actions */}
      <div className="flex items-center space-x-4">
        <Button
          variant="outline"
          size="sm"
          className="hidden md:flex gradient-primary border-0 text-white hover:opacity-90 bg-transparent"
        >
          <Upload className="w-4 h-4 mr-2" />
          Upload
        </Button>

        <Button variant="ghost" size="sm" className="relative">
          <Bell className="w-5 h-5" />
          <span className="absolute -top-1 -right-1 w-3 h-3 bg-orange-500 rounded-full"></span>
        </Button>

        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="sm" className="rounded-full">
              <div className="w-8 h-8 bg-gradient-to-r from-purple-500 to-orange-500 rounded-full flex items-center justify-center">
                <User className="w-4 h-4" />
              </div>
            </Button>
          </DropdownMenuTrigger>
<DropdownMenuContent align="end" className="w-56 bg-gray-800 border-gray-700">
  {user ? (
    <>
      <DropdownMenuItem>
        <User className="mr-2 h-4 w-4" />
        Profile
      </DropdownMenuItem>
      <DropdownMenuItem>
        Settings
      </DropdownMenuItem>
      <DropdownMenuItem>
        Help
      </DropdownMenuItem>
      <DropdownMenuSeparator className="bg-gray-700" />
      <DropdownMenuItem onClick={logout}>
        Log out
      </DropdownMenuItem>
    </>
  ) : (
    <>
      <DropdownMenuItem onClick={onLoginClick}>
        <User className="mr-2 h-4 w-4" />
        Login / Sign Up
      </DropdownMenuItem>
      <DropdownMenuSeparator className="bg-gray-700" />
      <DropdownMenuItem>
        Help
      </DropdownMenuItem>
    </>
  )}
</DropdownMenuContent>

        </DropdownMenu>
      </div>
    </header>
  )
}
