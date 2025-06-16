package com.unicesumar.film_list.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String senha;
    private String nome;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FilmeNaoAssistido> listaFilmesNaoAssistidos = new ArrayList<FilmeNaoAssistido>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FilmeAssistido> listaFilmesAssistidos = new ArrayList<FilmeAssistido>();

    public Usuario() {}
    
    public Usuario(String email, String senha, String nome) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    public Usuario(Long id) {
        this.id = id;
    }

    // getter
    public Long getId() {
        return id;
    }

    // getter e setter email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    // getter e setter senha
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    // getter e setter nome
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<FilmeNaoAssistido> listarFilmesNaoAssistidos() {
        return listaFilmesNaoAssistidos;
    }

    public void setListaFilmesNaoAssistidos(ArrayList<FilmeNaoAssistido> listaFilmesNaoAssistidos) {
        this.listaFilmesNaoAssistidos = listaFilmesNaoAssistidos;
    }

    public void addFilmeNaoAssistido(FilmeNaoAssistido filme) {
        listaFilmesNaoAssistidos.add(filme);
    }

    public void removerFilmeNaoAssistido(int indexFilme) {
        listaFilmesNaoAssistidos.remove(indexFilme);
    }

    public List<FilmeAssistido> listarFilmesAssistidos() {
        return listaFilmesAssistidos;
    }

    public void setListaFilmesAssistidos(ArrayList<FilmeAssistido> listaFilmesAssistidos) {
        this.listaFilmesAssistidos = listaFilmesAssistidos;
    }

    public void addFilmeAssistido(FilmeAssistido filme) {
        listaFilmesAssistidos.add(filme);
    }

    public void removerFilmeAssistido(int indexFilme) {
        listaFilmesAssistidos.remove(indexFilme);
    }
}
