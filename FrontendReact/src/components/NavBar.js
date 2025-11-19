import React, { useContext } from "react";
import { Link } from "react-router-dom";
import ThemeToggle from "./ThemeToggle";
import { AuthContext } from "../context/AuthContext";

export default function NavBar() {
  const { isAuthenticated, user, logout } = useContext(AuthContext || {});

  return (
    <header className="navbar">
      <div className="brand">
        <div className="logo">B</div>
        <Link to="/books" className="title brand-link">Biblioteca</Link>
      </div>

      <div className="nav-right">
        <Link to="/books" className="nav-link">Livros</Link>
        {isAuthenticated ? (
          <>
            <Link to="/dashboard" className="nav-link">Dashboard</Link>
            <span className="muted" style={{marginLeft:6}}>{user?.username}</span>
            <button className="btn btn-ghost" onClick={logout}>Sair</button>
          </>
        ) : (
          <>
            <Link to="/login" className="nav-link">Entrar</Link>
            <Link to="/register" className="nav-link">Registrar</Link>
          </>
        )}
        <ThemeToggle />
      </div>
    </header>
  );
}
