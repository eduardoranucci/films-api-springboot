package com.unicesumar.film_list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/registro")
    public String registro(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/validarRegistro")
    public String validarRegistro(@ModelAttribute Usuario user, Model modelo) {
        
        user.setEmail(user.getEmail().trim());
        user.setNome(user.getNome().trim());
        user.setSenha(user.getSenha().trim());

        if (user.getEmail() == null || user.getEmail().isEmpty() ||
            user.getNome() == null || user.getNome().isEmpty() ||
            user.getSenha() == null || user.getSenha().isEmpty()) 
        {
            modelo.addAttribute("msg", "Preencha todos os campos!");
            modelo.addAttribute("usuario", user);
            return "registro";
        }

        if (authService.emailJaCadastrado(user.getEmail())) {
            modelo.addAttribute("msg", "Já existe um usuário com esse e-mail!");
            modelo.addAttribute("usuario", user);
            return "registro";
        }

        authService.adicionarUsuario(user);
        modelo.addAttribute("msgSuccess", "Usuário cadastrado com sucesso!");
        return "index";
    }
}
