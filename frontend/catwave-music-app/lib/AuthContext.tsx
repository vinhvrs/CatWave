"use client";

import React, { createContext, useState, useContext, useEffect, ReactNode } from "react";
import api from "./api"; // adjust path if needed

interface User {
  id: number;
  username: string;
  // add other user fields if needed
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);

  // Load token + user from localStorage on first load
  useEffect(() => {
  const storedToken = localStorage.getItem("token");
  const storedUser = localStorage.getItem("user");

  if (storedToken && storedToken !== "undefined") {
    setToken(storedToken);
  }

  if (storedUser && storedUser !== "undefined") {
    try {
      setUser(JSON.parse(storedUser));
    } catch (error) {
      console.error("Failed to parse stored user:", error);
      localStorage.removeItem("user"); // ✅ clear nếu lỗi
    }
  } else {
    localStorage.removeItem("user"); // ✅ clear nếu value là "undefined"
  }
}, []);

  // Axios interceptor to attach token automatically
  useEffect(() => {
    if (!token) return;

    const requestInterceptor = api.interceptors.request.use((config) => {
      config.headers.Authorization = `Bearer ${token}`;
      return config;
    });

    return () => {
      api.interceptors.request.eject(requestInterceptor);
    };
  }, [token]);

  const login = async (username: string, password: string) => {
    try {
      const res = await api.post("/auth/login", { username, password });
      const data = res.data;

      // Assuming backend returns { token: "...", user: {...} }
   //   setToken(data.token);
      setUser(data.user); // adjust if backend doesn't return user info

    //  localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify(data.user));

      console.log("Login success:", data);
    } catch (err: any) {
      console.error("Login failed:", err);
      throw err;
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
