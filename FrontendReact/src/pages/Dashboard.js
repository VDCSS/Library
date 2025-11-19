import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import api from "../services/api";

export default function Dashboard() {
  const { user, logout } = useContext(AuthContext);
  const [loans, setLoans] = useState([]);

  useEffect(() => {
    fetchLoans();
  }, []);

  const fetchLoans = async () => {
    try {
      // backend endpoint example: /loans/person/{personId}
      const res = await api.get(`/loans/person/${user?.id || ""}`);
      setLoans(res.data || []);
    } catch (err) {
      // maybe backend returns loans for token user without id param
      try {
        const res2 = await api.get("/loans");
        setLoans(res2.data || []);
      } catch (e) {
        setLoans([]);
      }
    }
  };

  return (
    <div>
      <h2>Dashboard</h2>
      <p>Bem-vindo, <strong>{user?.username}</strong></p>
      <button onClick={logout}>Sair</button>

      <h3>Meus Empréstimos</h3>
      <ul>
        {loans.map((l) => (
          <li key={l.id}>
            {l.book?.title} - Status: {l.status} - Vencimento: {l.dueDate}
          </li>
        ))}
      </ul>
    </div>
  );
}
