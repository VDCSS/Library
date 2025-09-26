package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookDTO {

    private Long id;

    @NotBlank(message = "Título obrigatório")
    private String titulo;

    @NotBlank(message = "Autor obrigatório")
    private String autor;

    @NotNull
    private Boolean emprestado;

    private String emprestadoPara;

    public BookDTO() {}
    public BookDTO(Long id, String titulo, String autor, Boolean emprestado, String emprestadoPara) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.emprestado = emprestado;
        this.emprestadoPara = emprestadoPara;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public Boolean getEmprestado() { return emprestado; }
    public void setEmprestado(Boolean emprestado) { this.emprestado = emprestado; }

    public String getEmprestadoPara() { return emprestadoPara; }
    public void setEmprestadoPara(String emprestadoPara) { this.emprestadoPara = emprestadoPara; }
}
