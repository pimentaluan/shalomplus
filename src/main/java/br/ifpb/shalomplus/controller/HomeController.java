package br.ifpb.shalomplus.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.ifpb.shalomplus.model.Aluno;
import br.ifpb.shalomplus.model.Profissional;
import br.ifpb.shalomplus.repository.AtendimentoRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @GetMapping("/home")
    public ModelAndView home(HttpSession session, ModelAndView model) {
        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        if (usuario == null) {
            model.setViewName("redirect:/auth");
            return model;
        }

        model.addObject("usuario", usuario);
        model.addObject("tipoUsuario", tipoUsuario);

        LocalDateTime agora = LocalDateTime.now();

        if ("ALUNO".equals(tipoUsuario)) {
            Aluno aluno = (Aluno) usuario;

            model.addObject("proximosAtendimentos",
                atendimentoRepository.findByAluno_IdAndCanceladoFalseAndDataHoraGreaterThanEqualOrderByDataHoraAsc(aluno.getId(), agora));

            model.addObject("atendimentosAntigos",
                atendimentoRepository.findByAluno_IdAndDataHoraLessThanOrderByDataHoraDesc(aluno.getId(), agora));
        }

        else if ("PROFISSIONAL".equals(tipoUsuario)) {
            Profissional prof = (Profissional) usuario;

            model.addObject("proximosAtendimentos",
                atendimentoRepository.findByProfissional_IdAndCanceladoFalseAndDataHoraGreaterThanEqualOrderByDataHoraAsc(prof.getId(), agora));

            model.addObject("atendimentosAntigos",
                atendimentoRepository.findByProfissional_IdAndDataHoraLessThanOrderByDataHoraDesc(prof.getId(), agora));
        }

        else if ("SECRETARIO".equals(tipoUsuario)) {
            model.addObject("atendimentos",
                atendimentoRepository.findAllByOrderByDataHoraDesc());
        }

        model.setViewName("home");
        return model;
    }
}
