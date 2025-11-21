import React from "react";
import { Routes, Route } from "react-router-dom";
import NavBar from "./components/NavBar";
import LivroList from "./LivroList";
import LivroForm from "./LivroForm";
import Login from "./pages/Login";
import Register from "./pages/Register";
import "./styles.css";

function App() {
  return (
    <>
      <NavBar />

      <Routes>
        <Route path="/" element={<LivroList />} />
        <Route path="/books" element={<LivroList />} />
        <Route path="/add" element={<LivroForm />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </>
  );
}

export default App;
