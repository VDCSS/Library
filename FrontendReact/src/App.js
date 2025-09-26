import React, { useEffect, useState } from "react";
import axios from "axios";

const API = "http://localhost:8080/livros";

function App() {
  const [livros, setLivros] = useState([]);
  const [titulo, setTitulo] = useState("");
  const [autor, setAutor] = useState("");
  const [emprestado, setEmprestado] = useState(false);
  const [emprestadoPara, setEmprestadoPara] = useState("");
  const [buscaTitulo, setBuscaTitulo] = useState("");
  const [buscaAutor, setBuscaAutor] = useState("");
  const [buscaEmprestado, setBuscaEmprestado] = useState("");

  const carregarLivros = async () => {
    try {
      const res = await axios.get(API);
      setLivros(res.data.content || res.data);
    } catch (err) {
      alert("Erro ao carregar livros");
    }
  };

  useEffect(() => { carregarLivros(); }, []);

  const cadastrar = async () => {
    if (emprestado && !emprestadoPara) {
      alert("Informe para quem está emprestado");
      return;
    }
    try {
      await axios.post(API, { titulo, autor, emprestado, emprestadoPara });
      setTitulo(""); setAutor(""); setEmprestado(false); setEmprestadoPara("");
      carregarLivros();
    } catch {
      alert("Erro ao cadastrar livro");
    }
  };

  const deletar = async (id) => {
    try { await axios.delete(`${API}/${id}`); carregarLivros(); }
    catch { alert("Erro ao deletar livro"); }
  };

  const buscar = async () => {
    try {
      let url = `${API}/buscar?`;
      if (buscaTitulo) url += `titulo=${buscaTitulo}&`;
      if (buscaAutor) url += `autor=${buscaAutor}&`;
      if (buscaEmprestado) url += `emprestado=${buscaEmprestado}&`;
      const res = await axios.get(url);
      setLivros(res.data.content || res.data);
    } catch { alert("Erro na busca"); }
  };

  return (
    <div>
      <h1>Biblioteca</h1>
      <h2>Cadastrar Livro</h2>
      <input placeholder="Título" value={titulo} onChange={e => setTitulo(e.target.value)} />
      <input placeholder="Autor" value={autor} onChange={e => setAutor(e.target.value)} />
      <label>
        Emprestado: <input type="checkbox" checked={emprestado} onChange={e=>setEmprestado(e.target.checked)} />
      </label>
      <input placeholder="Emprestado para" value={emprestadoPara} onChange={e=>setEmprestadoPara(e.target.value)} />
      <button onClick={cadastrar}>Cadastrar</button>

      <h2>Buscar</h2>
      <input placeholder="Título" value={buscaTitulo} onChange={e=>setBuscaTitulo(e.target.value)} />
      <input placeholder="Autor" value={buscaAutor} onChange={e=>setBuscaAutor(e.target.value)} />
      <select value={buscaEmprestado} onChange={e=>setBuscaEmprestado(e.target.value)}>
        <option value="">Todos</option>
        <option value="true">Emprestados</option>
        <option value="false">Disponíveis</option>
      </select>
      <button onClick={buscar}>Buscar</button>
      <button onClick={carregarLivros}>Limpar Busca</button>

      <h2>Lista de Livros</h2>
      <ul>
        {livros.map(l => (
          <li key={l.id}>
            {l.titulo} - {l.autor} - {l.emprestado ? `Emprestado (${l.emprestadoPara})` : "Disponível"}
            <button onClick={()=>deletar(l.id)}>Excluir</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
