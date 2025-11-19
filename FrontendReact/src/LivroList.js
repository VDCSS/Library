import React, { useState, useEffect, useContext } from "react";
import api from "./services/api"; // ajusta se o caminho for ../services/api
import { AuthContext } from "./context/AuthContext";

export default function LivroList() {
  const [livros, setLivros] = useState([]);
  const [q, setQ] = useState("");
  const [sortBy, setSortBy] = useState("title");
  const [loading, setLoading] = useState(false);
  const { isAuthenticated } = useContext(AuthContext || {});

  useEffect(() => {
    load();
  }, []);

  async function load() {
    setLoading(true);
    try {
      const res = await api.get("/books");
      setLivros(res.data || []);
    } catch (e) {
      console.error(e);
      setLivros([]);
    } finally {
      setLoading(false);
    }
  }

  function filtered() {
    const term = q.trim().toLowerCase();
    let arr = livros.slice();
    if (term) {
      arr = arr.filter(b =>
        (b.title || "").toLowerCase().includes(term) ||
        (b.author || "").toLowerCase().includes(term) ||
        (b.isbn || "").toLowerCase().includes(term)
      );
    }
    if (sortBy === "title") arr.sort((a,b)=> (a.title||"").localeCompare(b.title||""));
    if (sortBy === "author") arr.sort((a,b)=> (a.author||"").localeCompare(b.author||""));
    if (sortBy === "available") arr.sort((a,b)=> ( (b.copies||0) - (a.copies||0) ));
    return arr;
  }

  async function borrow(bookId) {
    try {
      // payload assumed {bookId, days}
      await api.post("/loans", { bookId, days: 7 });
      await load();
      alert("Empréstimo realizado com sucesso!");
    } catch (err) {
      const message = err.response?.data?.error || err.message || "Erro ao emprestar";
      alert(message);
    }
  }

  return (
    <div className="app-container">
      <div className="header-row">
        <div>
          <h1 className="page-title">Livros</h1>
          <p className="hint">Encontre e empreste livros. Use pesquisa e ordenação.</p>
        </div>

        <div className="controls">
          <div className="search card">
            <svg width="16" height="16" viewBox="0 0 24 24" aria-hidden style={{opacity:0.7}}>
              <path fill="currentColor" d="M21 20l-5.6-5.6a7 7 0 10-1.4 1.4L20 21z"></path>
            </svg>
            <input value={q} onChange={(e)=>setQ(e.target.value)} placeholder="Pesquisar por título, autor ou ISBN" />
          </div>

          <select className="select" value={sortBy} onChange={(e)=>setSortBy(e.target.value)}>
            <option value="title">Ordenar por título</option>
            <option value="author">Ordenar por autor</option>
            <option value="available">Mais disponíveis</option>
          </select>
        </div>
      </div>

      <section className="book-grid">
        {loading && <div className="card">Carregando...</div>}
        {!loading && filtered().length === 0 && <div className="card">Nenhum livro encontrado.</div>}

        {filtered().map((b) => (
          <article className="card book-card fade-in" key={b.id}>
            <div style={{display:"flex",alignItems:"center",justifyContent:"space-between"}}>
              <div>
                <h2 className="book-title">{b.title}</h2>
                <p className="book-author">{b.author}</p>
              </div>
              <div className="badge">{(b.copies !== undefined) ? `${b.copies} disponíveis` : (b.available ? "Disponível" : "Indisponível")}</div>
            </div>

            <p className="muted">ISBN: {b.isbn || "—"}</p>

            <div style={{marginTop:"auto", display:"flex", gap:8, justifyContent:"space-between", alignItems:"center"}}>
              <div className="muted">Emprestado: {b.timesBorrowed || 0}x</div>
              <div>
                <button
                  className="btn btn-primary"
                  disabled={!isAuthenticated || (b.copies !== undefined && b.copies <= 0)}
                  onClick={()=>borrow(b.id)}
                >
                  Emprestar
                </button>
                <button className="btn btn-ghost" style={{marginLeft:8}} onClick={()=>alert("Abrir página de detalhes (não implementado)")}>
                  Detalhes
                </button>
              </div>
            </div>
          </article>
        ))}
      </section>
    </div>
  );
}
