package com.unicesumar.film_list.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unicesumar.film_list.model.FilmeNaoAssistido;
import com.unicesumar.film_list.model.FilmeAssistido;
import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.repository.UsuarioRepository;
import com.unicesumar.film_list.service.FilmeService;
import com.unicesumar.film_list.request.AssistirFilmeRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Filmes", description = "Endpoints relacionadas a filmes")
@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Listar filmes não assistidos", description = "Retorna todos os filmes não assistidos do usuário autenticado.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/nao-assistidos")
    public List<FilmeNaoAssistido> listarFilmesNaoAssistidos(@Parameter(hidden = true) Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        return filmeService.listarFilmesNaoAssistidos(usuario.getId());
    }

    @Operation(summary = "Listar filmes assistidos", description = "Retorna todos os filmes assistidos do usuário autenticado.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/assistidos")
    public List<FilmeAssistido> listarFilmesAssistidos(@Parameter(hidden = true) Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        return filmeService.listarFilmesAssistidos(usuario.getId());
    }

    @Operation(
        summary = "Adicionar filme não assistido",
        description = "Adiciona um novo filme não assistido para o usuário autenticado.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do filme a ser adicionado",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = FilmeNaoAssistido.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\n  \"titulo\": \"Star Wars: Episódio III - A Vingança dos Sith\",\n  \"genero\": \"Ação/Aventura\",\n  \"anoDeLancamento\": 2005\n}"
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Filme criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Filme já cadastrado")
    })
    @PostMapping
    public ResponseEntity<?> adicionarFilme(@Parameter(hidden = true) Principal principal, @RequestBody FilmeNaoAssistido filme) {
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

    @Operation(summary = "Marcar filme como assistido", description = "Marca um filme não assistido como assistido, informando a data.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Filme marcado como assistido"),
        @ApiResponse(responseCode = "400", description = "Data não informada"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @PutMapping("/{id}/assistir")
    public ResponseEntity<?> assistirFilme(
            @Parameter(description = "ID do filme não assistido") @PathVariable Long id,
            @RequestBody AssistirFilmeRequest request,
            @Parameter(hidden = true) Principal principal) {

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

    @Operation(summary = "Deletar filme não assistido", description = "Remove um filme não assistido do usuário autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Filme deletado com sucesso"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarFilme(
            @Parameter(description = "ID do filme não assistido") @PathVariable Long id,
            @Parameter(hidden = true) Principal principal) {
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