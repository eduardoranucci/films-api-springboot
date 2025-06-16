package com.unicesumar.film_list.model;

import jakarta.persistence.Entity;

@Entity
public class FilmeNaoAssistido extends Filme {
    
    public FilmeNaoAssistido() {
        super();
    }

    public FilmeNaoAssistido(String titulo, String genero, Integer anoDeLancamento) {
        super(titulo, genero, anoDeLancamento);
    }
}