-- Books
CREATE TABLE IF NOT EXISTS books (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  genre VARCHAR(100),
  quantity INT DEFAULT 1,
  added_date DATE,
  available BOOLEAN DEFAULT TRUE,
  times_borrowed INT DEFAULT 0
);

-- Persons
CREATE TABLE IF NOT EXISTS persons (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  cpf VARCHAR(20) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(30)
);

-- Loans
CREATE TABLE IF NOT EXISTS loans (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  book_id BIGINT NOT NULL,
  person_id BIGINT NOT NULL,
  loan_date TIMESTAMP,
  return_date TIMESTAMP,
  status VARCHAR(20),
  CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES books(id),
  CONSTRAINT fk_loan_person FOREIGN KEY (person_id) REFERENCES persons(id)
);

INSERT INTO users (id, name, email, password, role, blocked, outstanding_fines)
VALUES (1, 'Admin', 'admin@local', '$2a$10$u1qk...REPLACE_WITH_BCRYPT', 'ADMIN', false, 0.0);
