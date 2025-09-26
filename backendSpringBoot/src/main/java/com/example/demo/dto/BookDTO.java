package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class BookDTO {

    private Long id;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    private String autor;

    private String emprestadoPara;

    public BookDTO() {}

    public BookDTO(Long id, String titulo, String autor, String emprestadoPara) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.emprestadoPara = emprestadoPara;
    }

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEmprestadoPara() { return emprestadoPara; }
    public void setEmprestadoPara(String emprestadoPara) { this.emprestadoPara = emprestadoPara; }
}