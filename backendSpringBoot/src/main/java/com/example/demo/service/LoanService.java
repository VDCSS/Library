package com.example.demo.service;

import com.example.demo.dto.LoanRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.model.Student;
import com.example.demo.repository.LoanRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository repository;
    private final BookService bookService;
    private final StudentService studentService;

    public LoanService(LoanRepository repository, BookService bookService, StudentService studentService) {
        this.repository = repository;
        this.bookService = bookService;
        this.studentService = studentService;
    }

    public List<Loan> findAll() {
        return repository.findAll();
    }

    public Loan createLoan(LoanRequest request) {
        Book book = bookService.findById(request.getBookId());
        Student student = studentService.findById(request.getStudentId());

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book not available for loan");
        }

        bookService.updateAvailability(book.getId(), false);

        Loan loan = Loan.builder()
                .book(book)
                .student(student)
                .loanDate(LocalDate.now())
                .build();

        return repository.save(loan);
    }

    public void returnBook(Long loanId) {
        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        loan.setReturnDate(LocalDate.now());
        bookService.updateAvailability(loan.getBook().getId(), true);
        repository.save(loan);
    }
}
