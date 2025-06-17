package com.unicesumar.film_list.response;

public class FilmeResponse {
    private Long id;
    private String titulo;
    private String genero;
    private Integer anoDeLancamento;

    public FilmeResponse(Long id, String titulo, String genero, Integer anoDeLancamento) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.anoDeLancamento = anoDeLancamento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getAnoDeLancamento() { return anoDeLancamento; }
    public void setAnoDeLancamento(Integer anoDeLancamento) { this.anoDeLancamento = anoDeLancamento; }
}
