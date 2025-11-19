import React, { createContext, useState, useEffect } from "react";
import authService from "../services/authService";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => authService.getUserFromStorage());
  const [token, setToken] = useState(() => authService.getTokenFromStorage());

  useEffect(() => {
    authService.setAuth(token, user);
  }, [token, user]);

  const login = (token, userInfo) => {
    setToken(token);
    setUser(userInfo);
    authService.saveToken(token);
    authService.saveUser(userInfo);
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    authService.clearAuth();
  };

  const value = {
    user,
    token,
    login,
    logout,
    isAuthenticated: !!token
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
