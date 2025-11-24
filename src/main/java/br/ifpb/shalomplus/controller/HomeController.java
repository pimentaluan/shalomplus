package br.ifpb.shalomplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.ifpb.shalomplus.model.Aluno;
import br.ifpb.shalomplus.model.Profissional;
import jakarta.servlet.http.HttpSession;
import br.ifpb.shalomplus.repository.AtendimentoRepository;

@Controller
public class HomeController {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @GetMapping("/home")
    public ModelAndView home(HttpSession session, ModelAndView model) {
        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        // Redireciona para login se não houver usuário
        if (usuario == null) {
            model.setViewName("redirect:/auth");
            return model;
        }

        model.addObject("usuario", usuario);
        model.addObject("tipoUsuario", tipoUsuario);

        // Adiciona a lista de atendimentos conforme o tipo de usuário
        if ("ALUNO".equals(tipoUsuario)) {
            Aluno aluno = (Aluno) usuario;
            model.addObject("atendimentos", atendimentoRepository.findByAluno_Id(aluno.getId()));
        } else if ("PROFISSIONAL".equals(tipoUsuario)) {
            Profissional prof = (Profissional) usuario;
            model.addObject("atendimentos", atendimentoRepository.findByProfissional_Id(prof.getId()));
        } else if ("SECRETARIO".equals(tipoUsuario)) {
            model.addObject("atendimentos", atendimentoRepository.findAll());
        }

        model.setViewName("home"); // Nome do template Thymeleaf home.html
        return model;
    }
}
