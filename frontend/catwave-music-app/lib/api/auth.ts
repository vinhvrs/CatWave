// lib/api/auth.ts

export const login = async (username: string, password: string) => {
  const res = await fetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }), // ✅ đổi email -> username
  });
  if (!res.ok) {
    throw new Error("Login failed");
  }
  return res.json();
};

export const register = async (name: string, email: string, password: string) => {
  const res = await fetch("/api/auth/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, email, password }),
  });
  if (!res.ok) {
    throw new Error("Register failed");
  }
  return res.json();
};
