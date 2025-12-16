package br.ifpb.shalomplus.controller;

import br.ifpb.shalomplus.model.Secretario;
import br.ifpb.shalomplus.repository.SecretarioRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/secretario")
public class SecretarioCrudController {

    @Autowired
    private SecretarioRepository secretarioRepository;

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("tipoUsuario"));
    }

    // Listar secretários
    @GetMapping("/listar")
    public ModelAndView listar(HttpSession session) {
        if (!isAdmin(session)) return new ModelAndView("redirect:/auth");

        ModelAndView mv = new ModelAndView("secretario/listar");
        mv.addObject("secretarios", secretarioRepository.findAll());
        return mv;
    }

    // Form novo secretário
    @GetMapping("/novo")
    public ModelAndView novo(HttpSession session) {
        if (!isAdmin(session)) return new ModelAndView("redirect:/auth");

        ModelAndView mv = new ModelAndView("secretario/form");
        mv.addObject("secretario", new Secretario());
        return mv;
    }

    // Salvar (novo ou editar)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Secretario secretario, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth";

        secretarioRepository.save(secretario);
        return "redirect:/secretario/listar";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return new ModelAndView("redirect:/auth");

        ModelAndView mv = new ModelAndView("secretario/form");
        mv.addObject("secretario", secretarioRepository.findById(id).orElse(new Secretario()));
        return mv;
    }

    // Excluir
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth";

        secretarioRepository.deleteById(id);
        return "redirect:/secretario/listar";
    }
}
