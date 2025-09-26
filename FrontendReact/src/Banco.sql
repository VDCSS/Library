CREATE DATABASE biblioteca;
USE biblioteca;

CREATE TABLE livros (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  autor VARCHAR(255) NOT NULL,
  emprestado BOOLEAN NOT NULL,
  emprestado_para VARCHAR(255)
);

--INSERT INTO livros (titulo, autor, emprestado, emprestado_para) VALUES
--('Dom Casmurro', 'Machado de Assis', false, null),
--('O Senhor dos Anéis', 'J.R.R. Tolkien', true, 'Carlos'),
--('1984', 'George Orwell', false, null);
