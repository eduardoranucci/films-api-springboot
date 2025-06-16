package com.unicesumar.film_list.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicesumar.film_list.model.FilmeNaoAssistido;
import java.util.List;
import java.util.Optional;


public interface FilmeNaoAssistidoRepository extends JpaRepository<FilmeNaoAssistido, Long> {

    Optional<FilmeNaoAssistido> findById(Long id);
    List<FilmeNaoAssistido> findByUsuarioId(Long usuarioId);
    boolean existsByTituloIgnoreCaseAndUsuarioId(String titulo, Long usuarioId);
}
