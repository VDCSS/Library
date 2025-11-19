import axios from "axios";
import authService from "./authService";

const baseURL = process.env.REACT_APP_API_URL || "http://localhost:8080";

const api = axios.create({
  baseURL: baseURL + "/api",
  headers: {
    "Content-Type": "application/json"
  }
});

// request interceptor — add token if exists
api.interceptors.request.use(
  (config) => {
    const token = authService.getTokenFromStorage();
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// response interceptor — optional: handle 401 globally
api.interceptors.response.use(
  (resp) => resp,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Optionally: clear auth and redirect to login
      authService.clearAuth();
      // window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;
