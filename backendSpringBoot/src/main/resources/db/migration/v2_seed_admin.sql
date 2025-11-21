-- V2__seed_admin.sql
INSERT INTO person (username, name, email, password, matricula)
VALUES ('admin', 'Administrador', 'admin@library.local', '$2a$10$Pr0fessor', NULL)
ON DUPLICATE KEY UPDATE username = username;

-- link role
INSERT INTO person_roles (person_id, role)
SELECT id, 'ROLE_ADMIN' FROM person WHERE username='admin'
ON DUPLICATE KEY UPDATE person_id = person_id;
