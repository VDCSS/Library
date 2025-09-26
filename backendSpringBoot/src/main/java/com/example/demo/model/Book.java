package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "livros")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título obrigatório")
    private String titulo;

    @NotBlank(message = "Autor obrigatório")
    private String autor;

    @NotNull
    private Boolean emprestado;

    private String emprestadoPara;

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
