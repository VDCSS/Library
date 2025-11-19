-- Persons
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255),
    password VARCHAR(255),
    phone VARCHAR(50)
);

CREATE TABLE person_roles (
    person_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT fk_person_roles_person FOREIGN KEY(person_id) REFERENCES person(id)
);

-- Books
CREATE TABLE book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(512) NOT NULL,
    author VARCHAR(255),
    isbn VARCHAR(50),
    copies INTEGER DEFAULT 1,
    times_borrowed INTEGER DEFAULT 0,
    version BIGINT
);

-- Loans
CREATE TABLE loan (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    start_date DATE,
    due_date DATE,
    returned_date DATE,
    status VARCHAR(50),
    CONSTRAINT fk_loan_person FOREIGN KEY(person_id) REFERENCES person(id),
    CONSTRAINT fk_loan_book FOREIGN KEY(book_id) REFERENCES book(id)
);
