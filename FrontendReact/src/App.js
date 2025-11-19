import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import NavBar from "./components/NavBar";
import LivroList from "./LivroList";
import LivroForm from "./LivroForm";
import Login from "./pages/Login"; // se tiver
import Register from "./pages/Register";
import "./styles.css";

function App(){
  return (
    <BrowserRouter>
      <NavBar />
      <Routes>
        <Route path="/" element={<LivroList />} />
        <Route path="/books" element={<LivroList />} />
        <Route path="/add" element={<LivroForm />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
