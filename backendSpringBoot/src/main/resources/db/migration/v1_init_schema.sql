-- V1__init_schema.sql
-- Create users (persons), books, loans, notifications, and roles collection table

CREATE TABLE IF NOT EXISTS person (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(255),
  email VARCHAR(255),
  password VARCHAR(255),
  matricula VARCHAR(11),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS person_roles (
  person_id BIGINT NOT NULL,
  role VARCHAR(50) NOT NULL,
  CONSTRAINT fk_person_roles_person FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_person_matricula ON person(matricula);

CREATE TABLE IF NOT EXISTS book (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(512) NOT NULL,
  author VARCHAR(255),
  isbn VARCHAR(50),
  total_quantity INT DEFAULT 1,
  available_quantity INT DEFAULT 1,
  times_borrowed INT DEFAULT 0,
  version BIGINT
);

CREATE TABLE IF NOT EXISTS loan (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  person_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  loan_time TIMESTAMP NULL,
  due_time TIMESTAMP NULL,
  return_time TIMESTAMP NULL,
  status VARCHAR(20),
  CONSTRAINT fk_loan_person FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE,
  CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notification (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  loan_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  type VARCHAR(50),
  message TEXT NOT NULL,
  is_read BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_notification_loan FOREIGN KEY (loan_id) REFERENCES loan(id) ON DELETE SET NULL
);
