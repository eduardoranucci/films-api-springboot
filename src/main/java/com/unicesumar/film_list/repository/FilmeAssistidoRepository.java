package com.unicesumar.film_list.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicesumar.film_list.model.FilmeAssistido;
import java.util.List;
import java.util.Optional;


public interface FilmeAssistidoRepository extends JpaRepository<FilmeAssistido, Long> {

    Optional<FilmeAssistido> findById(Long id);
    List<FilmeAssistido> findByUsuarioId(Long usuarioId);
    boolean existsByTituloIgnoreCaseAndUsuarioId(String titulo, Long usuarioId);
}
