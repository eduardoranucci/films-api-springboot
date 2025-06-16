package com.unicesumar.film_list.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class FilmeAssistido extends Filme {
    private LocalDate dataAssistido;

    public FilmeAssistido() {
        super();
    }

    public FilmeAssistido(String titulo, String genero, Integer anoDeLancamento, LocalDate dataAssistido) {
        super(titulo, genero, anoDeLancamento);
        this.dataAssistido = dataAssistido;
    }

    public LocalDate getDataAssistido() {
        return dataAssistido;
    }

    public void setDataAssistido(LocalDate dataAssistido) {
        this.dataAssistido = dataAssistido;
    }
}
