package com.unicesumar.film_list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Auth", description = "Endpoints relacionadas a autenticação")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
        summary = "Registrar novo usuário",
        description = "Cria um novo usuário na aplicação."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados do usuário para cadastro",
        required = true,
        content = @io.swagger.v3.oas.annotations.media.Content(
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Usuario.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{ \"nome\": \"Lukar\", \"email\": \"lukar@email.com\", \"senha\": \"123456\" }"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Preencha todos os campos!",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "409", description = "Já existe um usuário com esse e-mail!",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "text/plain"))
    })
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario user) {
        System.out.println("Dados recebidos para registro:");
        System.out.println(user.getEmail());
        System.out.println(user.getNome());
        System.out.println(user.getSenha());
        user.setEmail(user.getEmail() != null ? user.getEmail().trim() : null);
        user.setNome(user.getNome() != null ? user.getNome().trim() : null);
        user.setSenha(user.getSenha() != null ? user.getSenha().trim() : null);

        if (user.getEmail() == null || user.getEmail().isEmpty() ||
            user.getNome() == null || user.getNome().isEmpty() ||
            user.getSenha() == null || user.getSenha().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preencha todos os campos!");
        }

        if (authService.emailJaCadastrado(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um usuário com esse e-mail!");
        }

        authService.adicionarUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }
}
