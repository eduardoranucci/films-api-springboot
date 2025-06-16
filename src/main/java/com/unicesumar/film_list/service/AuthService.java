package com.unicesumar.film_list.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void adicionarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmailAndSenha(email, senha);
        if (usuario != null) {
            return usuario;
        }
        return null;
    }

    public boolean emailJaCadastrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
