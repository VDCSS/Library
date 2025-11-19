import React, { useEffect, useState, useContext } from "react";
import api from "../services/api";
import { AuthContext } from "../context/AuthContext";

export default function Books() {
  const [books, setBooks] = useState([]);
  const [error, setError] = useState(null);
  const { isAuthenticated } = useContext(AuthContext);

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const res = await api.get("/books");
      setBooks(res.data || []);
    } catch (err) {
      setError("Não foi possível carregar os livros.");
    }
  };

  const borrow = async (bookId) => {
    try {
      // Endpoint adapt if different: this example expects { personId, bookId, days }
      // If backend infers person from token, simply call POST /loans with {bookId, days}
      const payload = { bookId, days: 7 };
      await api.post("/loans", payload);
      alert("Empréstimo realizado com sucesso!");
      fetchBooks();
    } catch (err) {
      alert(err.response?.data?.error || "Erro ao emprestar");
    }
  };

  return (
    <div>
      <h2>Livros</h2>
      {error && <div className="error">{error}</div>}
      <div className="book-list">
        {books.map((b) => (
          <div key={b.id} className="book-card">
            <h3>{b.title}</h3>
            <p>{b.author}</p>
            <p>ISBN: {b.isbn}</p>
            <p>Disponíveis: {b.copies ?? (b.available ? "Sim" : "Não")}</p>
            <button disabled={!isAuthenticated || (b.copies !== undefined && b.copies <= 0)} onClick={() => borrow(b.id)}>
              {isAuthenticated ? "Emprestar" : "Entrar para emprestar"}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
