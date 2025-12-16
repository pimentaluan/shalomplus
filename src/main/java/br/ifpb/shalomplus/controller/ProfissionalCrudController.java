package br.ifpb.shalomplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import br.ifpb.shalomplus.model.Profissional;
import br.ifpb.shalomplus.repository.ProfissionalRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/profissional") // singular, igual ao aluno
public class ProfissionalCrudController {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("tipoUsuario"));
    }

    // Listar profissionais
    @GetMapping("/listar")
    public ModelAndView listar(HttpSession session) {
        if (!isAdmin(session)) return new ModelAndView("redirect:/auth");

        ModelAndView mv = new ModelAndView("profissional/listar"); // profissional/listar.html
        mv.addObject("profissionais", profissionalRepository.findAll());
        return mv;
    }

    // Form novo profissional
    @GetMapping("/novo")
    public ModelAndView novo(HttpSession session) {
        if (!isAdmin(session)) return new ModelAndView("redirect:/auth");

        ModelAndView mv = new ModelAndView("profissional/form"); // profissional/form.html
        mv.addObject("profissional", new Profissional());
        return mv;
    }

    // Salvar (novo ou editar)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Profissional profissional, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth";

        profissionalRepository.save(profissional);
        return "redirect:/profissional/listar";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return new ModelAndView("redirect:/auth");

        ModelAndView mv = new ModelAndView("profissional/form");
        mv.addObject("profissional", profissionalRepository.findById(id).orElse(new Profissional()));
        return mv;
    }

    // Excluir
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth";

        profissionalRepository.deleteById(id);
        return "redirect:/profissional/listar";
    }
}
