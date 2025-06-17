package com.unicesumar.film_list.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicesumar.film_list.model.FilmeNaoAssistido;
import com.unicesumar.film_list.model.FilmeAssistido;
import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.repository.FilmeNaoAssistidoRepository;
import com.unicesumar.film_list.repository.FilmeAssistidoRepository;

@Service
public class FilmeService {

    @Autowired
    private FilmeNaoAssistidoRepository filmeNaoAssistidoRepository;

    @Autowired
    private FilmeAssistidoRepository filmeAssistidoRepository;

    public boolean adicionarFilme(FilmeNaoAssistido filme, Long usuarioId) {
        filme.setUsuario(new Usuario(usuarioId));
        filmeNaoAssistidoRepository.save(filme);
        return true;
    }

    public List<FilmeNaoAssistido> listarFilmesNaoAssistidos(Long usuarioId) {
        return filmeNaoAssistidoRepository.findByUsuarioId(usuarioId);
    }

    public List<FilmeAssistido> listarFilmesAssistidos(Long usuarioId) {
        return filmeAssistidoRepository.findByUsuarioId(usuarioId);
    }

    public FilmeNaoAssistido buscarFilmeNaoAssistido(Long id) {
        FilmeNaoAssistido filme = filmeNaoAssistidoRepository.findById(id).orElse(null);
        return filme;
    }

    public FilmeAssistido buscarFilmeAssistido(Long id) {
        FilmeAssistido filme = filmeAssistidoRepository.findById(id).orElse(null);
        return filme;
    }

    public void assistirFilme(FilmeNaoAssistido filme, LocalDate dataAssistido) {
        FilmeAssistido filmeAssistido = new FilmeAssistido(
            filme.getTitulo(),
            filme.getGenero(),
            filme.getAnoDeLancamento(),
            dataAssistido
        );
        filmeAssistido.setUsuario(filme.getUsuario());

        filmeAssistidoRepository.save(filmeAssistido);
        Usuario usuario = filme.getUsuario();
        if (usuario != null) {
            usuario.listarFilmesNaoAssistidos().remove(filme);
        }
        filmeNaoAssistidoRepository.delete(filme);
    }

    public void deletarFilme(Long id) {
        FilmeNaoAssistido filme = filmeNaoAssistidoRepository.findById(id).orElse(null);
        if (filme != null) {
            Usuario usuario = filme.getUsuario();
            if (usuario != null) {
                usuario.listarFilmesNaoAssistidos().remove(filme);
            }
            filmeNaoAssistidoRepository.delete(filme);
        }
    }

    public boolean filmeJaCadastrado(String titulo, Long usuarioId) {
        boolean existeNaoAssistido = filmeNaoAssistidoRepository.existsByTituloIgnoreCaseAndUsuarioId(titulo, usuarioId);
        boolean existeAssistido = filmeAssistidoRepository.existsByTituloIgnoreCaseAndUsuarioId(titulo, usuarioId);
        return existeAssistido || existeNaoAssistido;
    }
}
