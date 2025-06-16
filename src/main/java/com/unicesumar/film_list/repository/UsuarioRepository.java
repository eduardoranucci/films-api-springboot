package com.unicesumar.film_list.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicesumar.film_list.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmailAndSenha(String email, String senha);
    Usuario findByEmail(String email);

    Usuario findByNome(String nome);
    boolean existsByEmail(String email);
}
