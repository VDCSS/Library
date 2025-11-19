import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../services/authService";

export default function Register() {
  const [payload, setPayload] = useState({ username: "", email: "", password: "" });
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => setPayload({ ...payload, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      await authService.register(payload);
      navigate("/login");
    } catch (err) {
      setError(err.response?.data?.error || "Erro ao registrar");
    }
  };

  return (
    <div className="auth-page">
      <h2>Registrar</h2>
      <form onSubmit={handleSubmit} className="form">
        <label>Usuário
          <input name="username" value={payload.username} onChange={handleChange} required />
        </label>
        <label>Email
          <input type="email" name="email" value={payload.email} onChange={handleChange} />
        </label>
        <label>Senha
          <input type="password" name="password" value={payload.password} onChange={handleChange} required />
        </label>
        <button type="submit">Registrar</button>
        {error && <div className="error">{error}</div>}
      </form>
    </div>
  );
}
