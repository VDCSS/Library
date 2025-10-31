package com.example.demo.dto;

import java.time.LocalDate;

public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String genre;
    private int quantity;
    private LocalDate addedDate;
    private boolean available;

    public BookDTO() {}

    public BookDTO(Long id, String title, String author, String genre, int quantity, LocalDate addedDate, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantity = quantity;
        this.addedDate = addedDate;
        this.available = available;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
