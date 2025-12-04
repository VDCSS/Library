-- V2: seed admin user (password must be bcrypt or you can register via /api/auth/register)
INSERT INTO users (id, name, email, password, role, blocked, outstanding_fines)
VALUES (1, 'root', 'admin@local', 'Pr0fessor', 'ADMIN', FALSE, 0.0);
