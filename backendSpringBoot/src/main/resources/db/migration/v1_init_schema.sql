-- V1: create tables base
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50),
  blocked BOOLEAN DEFAULT FALSE,
  outstanding_fines DOUBLE DEFAULT 0.0
);

CREATE TABLE books (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(512),
  author VARCHAR(255),
  isbn VARCHAR(100),
  year INT,
  category VARCHAR(255)
);

CREATE TABLE exemplars (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  book_id BIGINT,
  barcode VARCHAR(255) UNIQUE,
  status VARCHAR(50) DEFAULT 'AVAILABLE',
  CONSTRAINT fk_exemplar_book FOREIGN KEY (book_id) REFERENCES books(id)
);

CREATE TABLE loans (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  exemplar_id BIGINT,
  loan_date DATETIME,
  expected_return_date DATETIME,
  real_return_date DATETIME,
  renewals_remaining INT,
  renewals_done INT DEFAULT 0,
  returned BOOLEAN DEFAULT FALSE,
  days_overdue_at_return INT,
  fine_generated_id BIGINT,
  CONSTRAINT fk_loan_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_loan_exemplar FOREIGN KEY (exemplar_id) REFERENCES exemplars(id)
);

CREATE TABLE reservations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  book_id BIGINT,
  reserved_at DATETIME,
  notified BOOLEAN DEFAULT FALSE,
  expires_at DATETIME,
  CONSTRAINT fk_res_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_res_book FOREIGN KEY (book_id) REFERENCES books(id)
);

CREATE TABLE fines (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  loan_id BIGINT,
  created_at DATETIME,
  days_overdue INT,
  rate_per_day DECIMAL(10,2),
  amount DECIMAL(10,2),
  type VARCHAR(50),
  status VARCHAR(50),
  description TEXT,
  CONSTRAINT fk_fine_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_fine_loan FOREIGN KEY (loan_id) REFERENCES loans(id)
);

-- Indexes to help reports
CREATE INDEX idx_loans_user ON loans(user_id);
CREATE INDEX idx_fines_user ON fines(user_id);
CREATE INDEX idx_res_book ON reservations(book_id);
