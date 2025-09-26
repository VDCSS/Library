import React, { useState } from "react";
const API = "http://localhost:8080/livros";

function LivroForm({ onCadastrado }) {
  const [titulo, setTitulo] = useState("");
  const [autor, setAutor] = useState("");
  const [emprestadoPara, setEmprestadoPara] = useState("");
  const [loading, setLoading] = useState(false);

  async function cadastrar() {
    try {
      setLoading(true);
      const resp = await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ titulo, autor, emprestadoPara })
      });
      if (!resp.ok) {
        const body = await resp.json().catch(() => ({}));
        const msg = body && Object.values(body).length ? Object.values(body).join(", ") : "Erro ao cadastrar livro";
        throw new Error(msg);
      }
      setTitulo(""); setAutor(""); setEmprestadoPara("");
      onCadastrado();
    } catch (e) {
      alert(e.message);
    } finally { setLoading(false); }
  }

  return (
    <div>
      <h2>Cadastrar Livro</h2>
      <input placeholder="Título" value={titulo} onChange={e => setTitulo(e.target.value)} />
      <input placeholder="Autor" value={autor} onChange={e => setAutor(e.target.value)} />
      <input placeholder="Emprestado para" value={emprestadoPara} onChange={e => setEmprestadoPara(e.target.value)} />
      <button onClick={cadastrar} disabled={loading}>{loading ? "Enviando..." : "Cadastrar"}</button>
    </div>
  );
}

export default LivroForm;
