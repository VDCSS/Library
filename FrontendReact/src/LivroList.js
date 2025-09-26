import React from "react";
const API = "http://localhost:8080/livros";

function escapeRegExp(string) {
  return String(string).replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function highlight(text, term) {
  if (!term) return text;
  const re = new RegExp(`(${escapeRegExp(term)})`, 'i'); // sem 'g' para evitar estado
  const parts = String(text).split(re);
  const lowerTerm = term.toLowerCase();
  return parts.map((part, i) =>
    part.toLowerCase() === lowerTerm ? <mark key={i}>{part}</mark> : part
  );
}

function LivroList({ livros, onExcluido, termoBusca }) {
  async function excluir(id) {
    if (!window.confirm("Deseja excluir este livro?")) return;
    const resp = await fetch(`${API}/${id}`, { method: "DELETE" });
    if (!resp.ok) {
      // tenta ler mensagem de erro
      const body = await resp.json().catch(() => ({}));
      alert(body.error || "Erro ao excluir");
      return;
    }
    onExcluido();
  }

  return (
    <div>
      <h2>Lista de Livros</h2>
      {livros.length === 0 && <p>Nenhum livro encontrado.</p>}
      {livros.map(l => (
        <div key={l.id} style={{ border: "1px solid #ccc", margin: "5px", padding: "5px" }}>
          <div><b>{highlight(l.titulo || '', termoBusca)}</b> - {highlight(l.autor || '', termoBusca)}</div>
          <div>Emprestado para: {highlight(l.emprestadoPara || "Ninguém", termoBusca)}</div>
          <button onClick={() => excluir(l.id)}>Excluir</button>
        </div>
      ))}
    </div>
  );
}

export default LivroList;
