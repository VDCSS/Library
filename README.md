# Biblioteca - Projeto

## Backend
- Java 21, Spring Boot 3.x, H2 (dev), Spring Security, Lombok
- Rodar: abrir pasta `backend` no IntelliJ e executar `BibliotecaApplication`
- H2 console: http://localhost:8080/h2-console

## Frontend
- React + axios
- Rodar: `cd frontend` → `npm install` → `npm start`
- Frontend espera backend em `http://localhost:8080`

## Fluxos
- Registrar usuário: POST /users (public)
- Login: preencher usuário/senha no frontend (salva em localStorage para requests básicos)
- Admin endpoints (POST/PUT/DELETE /livros) requer ROLE_ADMIN (HTTP Basic)
- Solicitar empréstimo: POST /emprestimos (autenticado) com params livroId, usuarioId, retirada, devolucao
- Public: GET /livros/disponiveis and GET /emprestimos/publicos
