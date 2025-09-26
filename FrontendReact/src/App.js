import React, { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [livros, setLivros] = useState([]);
  const [titulo, setTitulo] = useState("");
  const [autor, setAutor] = useState("");
  const [emprestado, setEmprestado] = useState(false);
  const [emprestadoPara, setEmprestadoPara] = useState("");
  const [busca, setBusca] = useState("");

  const carregarLivros = async () => {
    try {
      const response = await axios.get("http://localhost:8080/livros");
      setLivros(response.data);
    } catch (err) {
      console.error("Erro ao carregar livros", err);
    }
  };

  useEffect(() => {
    carregarLivros();
  }, []);

  const cadastrarLivro = async () => {
    try {
      await axios.post("http://localhost:8080/livros", {
        titulo,
        autor,
        emprestado,
        emprestadoPara,
      });
      setTitulo("");
      setAutor("");
      setEmprestado(false);
      setEmprestadoPara("");
      carregarLivros();
    } catch (err) {
      console.error("Erro ao cadastrar livro", err);
    }
  };

  const deletarLivro = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/livros/${id}`);
      carregarLivros();
    } catch (err) {
      console.error("Erro ao deletar livro", err);
    }
  };

  const buscarLivros = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/livros/search?titulo=${busca}`);
      setLivros(response.data);
    } catch (err) {
      console.error("Erro ao buscar livros", err);
    }
  };

  return (
    <div>
      <h1>Biblioteca</h1>

      <h2>Cadastrar Livro</h2>
      <input value={titulo} onChange={(e) => setTitulo(e.target.value)} placeholder="Título" />
      <input value={autor} onChange={(e) => setAutor(e.target.value)} placeholder="Autor" />
      <label>
        Emprestado:
        <input type="checkbox" checked={emprestado} onChange={(e) => setEmprestado(e.target.checked)} />
      </label>
      <input value={emprestadoPara} onChange={(e) => setEmprestadoPara(e.target.value)} placeholder="Emprestado para" />
      <button onClick={cadastrarLivro}>Cadastrar</button>

      <h2>Buscar Livro</h2>
      <input value={busca} onChange={(e) => setBusca(e.target.value)} placeholder="Buscar por título" />
      <button onClick={buscarLivros}>Buscar</button>
      <button onClick={carregarLivros}>Limpar busca</button>

      <h2>Lista de Livros</h2>
      <ul>
        {livros.map((livro) => (
          <li key={livro.id}>
            {livro.titulo} - {livro.autor} - {livro.emprestado ? "Emprestado" : "Disponível"}{" "}
            {livro.emprestadoPara && `(${livro.emprestadoPara})`}
            <button onClick={() => deletarLivro(livro.id)}>Excluir</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
