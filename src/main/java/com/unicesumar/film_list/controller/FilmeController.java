package com.unicesumar.film_list.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unicesumar.film_list.model.FilmeNaoAssistido;
import com.unicesumar.film_list.model.FilmeAssistido;
import com.unicesumar.film_list.model.Usuario;
import com.unicesumar.film_list.repository.UsuarioRepository;
import com.unicesumar.film_list.service.FilmeService;

import java.time.LocalDate;

@Controller
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        List<FilmeNaoAssistido> filmesParaAssistir = filmeService.listarFilmesNaoAssistidos(usuario.getId());
        List<FilmeAssistido> filmesAssistidos = filmeService.listarFilmesAssistidos(usuario.getId());

        model.addAttribute("filmesParaAssistir", filmesParaAssistir);
        model.addAttribute("filmesAssistidos", filmesAssistidos);
        model.addAttribute("usuario", usuario);
        return "home";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model modelo) {
        modelo.addAttribute("filme", new FilmeNaoAssistido());
        return "cadastro";
    }

    @PostMapping("/adicionarFilme")
    public String adicionarFilme(Principal principal, @ModelAttribute FilmeNaoAssistido filme, Model model, RedirectAttributes attrs) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (filmeService.filmeJaCadastrado(filme.getTitulo(), usuario.getId())) {
            model.addAttribute("msg", "Filme já cadastrado");
            model.addAttribute("filme", filme);
            return "cadastro";
        }

        attrs.addFlashAttribute("msgSucesso", "Filme cadastrado com sucesso!");
        filmeService.adicionarFilme(filme, usuario.getId());
        return "redirect:/home";
    }

    @PostMapping("/adicionarData")
    public String adicionarData(
            @RequestParam("id") int id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAssistido,
            Principal principal, Model model,
            RedirectAttributes attrs) {

        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);

        FilmeNaoAssistido filme = filmeService.buscarFilmeNaoAssistido(id);
        if (filme != null) {
            filmeService.assistirFilme(filme, dataAssistido);
        }

        List<FilmeNaoAssistido> filmesParaAssistir = filmeService.listarFilmesNaoAssistidos(usuario.getId());
        List<FilmeAssistido> filmesAssistidos = filmeService.listarFilmesAssistidos(usuario.getId());

        attrs.addFlashAttribute("msgSucesso", "Data de visualização adicionada com sucesso. Filme movido para assistidos.");
        model.addAttribute("filmesParaAssistir", filmesParaAssistir);
        model.addAttribute("filmesAssistidos", filmesAssistidos);
        model.addAttribute("usuario", usuario);

        return "redirect:/home";
    }

    @PostMapping("/deletarFilme")
    public String deletarFilme(@RequestParam("id") Long id, Principal principal, Model model, RedirectAttributes attrs) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        
        filmeService.deletarFilme(id);

        List<FilmeNaoAssistido> filmesParaAssistir = filmeService.listarFilmesNaoAssistidos(usuario.getId());
        List<FilmeAssistido> filmesAssistidos = filmeService.listarFilmesAssistidos(usuario.getId());

        attrs.addFlashAttribute("msgSucesso", "Filme deletado com sucesso");
        model.addAttribute("filmesParaAssistir", filmesParaAssistir);
        model.addAttribute("filmesAssistidos", filmesAssistidos);
        model.addAttribute("usuario", usuario);

        return "redirect:/home";
    }

}