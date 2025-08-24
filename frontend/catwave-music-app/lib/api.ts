import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:1212/api", // ✅ đảm bảo đúng Spring Boot port + /api prefix nếu backend có
  withCredentials: true,
});

export default api;
