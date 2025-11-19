import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import authService from "../services/authService";
import { AuthContext } from "../context/AuthContext";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { login: loginContext } = useContext(AuthContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      const data = await authService.login(username, password);
      // token expected in data.token
      const token = data.token;
      // Optionally: fetch user profile if backend provides endpoint; we will set minimal user
      const userInfo = { username };
      loginContext(token, userInfo);
      navigate("/dashboard");
    } catch (err) {
      setError(err.response?.data?.error || "Erro ao fazer login");
    }
  };

  return (
    <div className="auth-page">
      <h2>Entrar</h2>
      <form onSubmit={handleSubmit} className="form">
        <label>Usuário
          <input value={username} onChange={(e) => setUsername(e.target.value)} required />
        </label>
        <label>Senha
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        </label>
        <button type="submit">Entrar</button>
        {error && <div className="error">{error}</div>}
      </form>
    </div>
  );
}
