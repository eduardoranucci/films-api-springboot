package com.unicesumar.film_list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario user) {
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
