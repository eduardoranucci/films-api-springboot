package com.unicesumar.film_list.controller;

import java.security.Principal;
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unicesumar.film_list.model.FilmeNaoAssistido;
import com.unicesumar.film_list.model.FilmeAssistido;
import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.repository.UsuarioRepository;
import com.unicesumar.film_list.service.FilmeService;
import com.unicesumar.film_list.request.AssistirFilmeRequest;

@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/nao-assistidos")
    public List<FilmeNaoAssistido> listarFilmesNaoAssistidos(Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        return filmeService.listarFilmesNaoAssistidos(usuario.getId());
    }

    @GetMapping("/assistidos")
    public List<FilmeAssistido> listarFilmesAssistidos(Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        return filmeService.listarFilmesAssistidos(usuario.getId());
    }

    @PostMapping
    public ResponseEntity<?> adicionarFilme(Principal principal, @RequestBody FilmeNaoAssistido filme) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (filme.getTitulo() == null || filme.getTitulo().trim().isEmpty() ||
            filme.getGenero() == null || filme.getGenero().trim().isEmpty() ||
            filme.getAnoDeLancamento() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preencha todos os campos obrigatórios: titulo, genero e anoDeLancamento.");
        }

        if (filmeService.filmeJaCadastrado(filme.getTitulo(), usuario.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Filme já cadastrado");
        }

        filmeService.adicionarFilme(filme, usuario.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(filme);
    }

    @PutMapping("/{id}/assistir")
    public ResponseEntity<?> assistirFilme(
            @PathVariable Long id,
            @RequestBody AssistirFilmeRequest request,
            Principal principal) {

        String email = principal.getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(email);
        FilmeNaoAssistido filme = filmeService.buscarFilmeNaoAssistido(id);
        if (filme == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado");
        }
        if (filme.getUsuario() == null || !filme.getUsuario().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para assistir este filme.");
        }
        if (request == null || request.getDataAssistido() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de assistido deve ser informada no corpo da requisição.");
        }
        filmeService.assistirFilme(filme, request.getDataAssistido());
        return ResponseEntity.ok("Filme marcado como assistido");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarFilme(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(email);
        FilmeNaoAssistido filme = filmeService.buscarFilmeNaoAssistido(id);
        if (filme == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Filme não encontrado");
        }
        if (filme.getUsuario() == null || !filme.getUsuario().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para deletar este filme.");
        }
        filmeService.deletarFilme(id);
        return ResponseEntity.ok("Filme deletado com sucesso");
    }
}