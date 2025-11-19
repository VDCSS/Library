import api from "./api";

const TOKEN_KEY = "biblioteca_token";
const USER_KEY = "biblioteca_user";

const login = async (username, password) => {
  const res = await api.post("/auth/login", { username, password });
  return res.data; // expected { token }
};

const register = async (payload) => {
  const res = await api.post("/auth/register", payload);
  return res.data;
};

const saveToken = (token) => {
  if (token) localStorage.setItem(TOKEN_KEY, token);
};

const saveUser = (user) => {
  if (user) localStorage.setItem(USER_KEY, JSON.stringify(user));
};

const getTokenFromStorage = () => localStorage.getItem(TOKEN_KEY);

const getUserFromStorage = () => {
  const raw = localStorage.getItem(USER_KEY);
  return raw ? JSON.parse(raw) : null;
};

const clearAuth = () => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
};

const setAuth = (token, user) => {
  if (token) saveToken(token);
  if (user) saveUser(user);
};

export default {
  login,
  register,
  saveToken,
  getTokenFromStorage,
  saveUser,
  getUserFromStorage,
  clearAuth,
  setAuth
};
