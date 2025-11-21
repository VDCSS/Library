-- V1__schema_with_notifications.sql

ALTER TABLE person
  ADD COLUMN IF NOT EXISTS matricula VARCHAR(11);

CREATE TABLE IF NOT EXISTS person_roles (
  person_id BIGINT NOT NULL,
  role VARCHAR(50) NOT NULL,
  CONSTRAINT fk_person_roles_person FOREIGN KEY(person_id) REFERENCES person(id)
);

-- Ensure matricula unique and exactly 11 digits enforced by check
ALTER TABLE person
  ADD CONSTRAINT uq_person_matricula UNIQUE (matricula);

ALTER TABLE person
  ADD CONSTRAINT chk_person_matricula_length CHECK (matricula IS NULL OR char_length(matricula) = 11);

CREATE TABLE IF NOT EXISTS book (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(512) NOT NULL,
  author VARCHAR(255),
  isbn VARCHAR(50),
  copies INTEGER DEFAULT 1,
  times_borrowed INTEGER DEFAULT 0,
  version BIGINT
);

-- Loans
CREATE TABLE IF NOT EXISTS loan (
  id BIGSERIAL PRIMARY KEY,
  person_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  start_time TIMESTAMP WITH TIME ZONE,
  due_date TIMESTAMP WITH TIME ZONE,
  returned_time TIMESTAMP WITH TIME ZONE,
  status VARCHAR(20),
  CONSTRAINT fk_loan_person FOREIGN KEY(person_id) REFERENCES person(id),
  CONSTRAINT fk_loan_book FOREIGN KEY(book_id) REFERENCES book(id)
);

-- Notifications table to store messages for admin
CREATE TABLE IF NOT EXISTS notification (
  id BIGSERIAL PRIMARY KEY,
  loan_id BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  type VARCHAR(30), -- e.g. 'LOAN_CREATED', 'LOAN_RETURNED'
  message TEXT NOT NULL,
  is_read BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_notification_loan FOREIGN KEY(loan_id) REFERENCES loan(id) ON DELETE SET NULL
);
