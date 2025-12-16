package br.ifpb.shalomplus.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import br.ifpb.shalomplus.model.Aluno;
import br.ifpb.shalomplus.model.Atendimento;
import br.ifpb.shalomplus.model.Profissional;
import br.ifpb.shalomplus.repository.AtendimentoRepository;
import br.ifpb.shalomplus.repository.AlunoRepository;
import br.ifpb.shalomplus.repository.ProfissionalRepository;

@Controller
@RequestMapping("/atendimento")
public class AtendimentoController {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    // Página de edição para psicólogo
    @GetMapping("/editar/{id}")
    public ModelAndView editarAtendimento(@PathVariable Long id, HttpSession session, ModelAndView model) {
        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        if (usuario == null || !"PROFISSIONAL".equals(tipoUsuario)) {
            model.setViewName("redirect:/auth");
            return model;
        }

        Atendimento atendimento = atendimentoRepository.findById(id).orElse(null);
        model.addObject("atendimento", atendimento);
        model.setViewName("atendimento");
        return model;
    }

    // Salvar observações e marcar atendimento como realizado
    @PostMapping("/editar")
    public String salvarObservacoes(
            @RequestParam Long id,
            @RequestParam String observacoes,
            @RequestParam(required = false) Boolean realizado,
            HttpSession session) {

        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        if (usuario == null || !"PROFISSIONAL".equals(tipoUsuario)) {
            return "redirect:/auth";
        }

        atendimentoRepository.findById(id).ifPresent(atendimento -> {
            atendimento.setObservacoes(observacoes);
            atendimento.setRealizado(realizado != null ? realizado : false);
            atendimentoRepository.save(atendimento);
        });

        return "redirect:/home";
    }

    // =====================
    // Secretário: criar novo atendimento
    // =====================
    @GetMapping("/novo")
    public ModelAndView novoAtendimento(HttpSession session, ModelAndView model) {
        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        if (usuario == null || !"SECRETARIO".equals(tipoUsuario)) {
            model.setViewName("redirect:/auth");
            return model;
        }

        model.addObject("alunos", alunoRepository.findAll());
        model.addObject("profissionais", profissionalRepository.findAll());
        model.addObject("atendimento", new Atendimento());
        model.setViewName("agendamento");
        return model;
    }

    @PostMapping("/novo")
    public String salvarAtendimento(
            @RequestParam Long alunoId,
            @RequestParam Long profissionalId,
            @RequestParam String tipo,
            @RequestParam String dataHora,
            HttpSession session) {

        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        if (usuario == null || !"SECRETARIO".equals(tipoUsuario)) {
            return "redirect:/auth";
        }

        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
        Profissional profissional = profissionalRepository.findById(profissionalId).orElse(null);

        if (aluno == null || profissional == null) {
            return "redirect:/atendimento/novo?erro=dados_invalidos";
        }

        LocalDateTime dh = LocalDateTime.parse(dataHora);

        boolean ocupado = atendimentoRepository.existsByProfissional_IdAndDataHoraAndCanceladoFalse(profissionalId, dh);
        if (ocupado) {
            return "redirect:/atendimento/novo?erro=indisponivel";
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setAluno(aluno);
        atendimento.setProfissional(profissional);
        atendimento.setTipo(tipo);
        atendimento.setRealizado(false);
        atendimento.setCancelado(false); 
        atendimento.setDataHora(dh);

        atendimentoRepository.save(atendimento);
        return "redirect:/home";
    }

    // =====================
    // Secretário: cancelar atendimento
    // =====================
    @GetMapping("/cancelar/{id}")
    public String cancelarAtendimento(@PathVariable Long id, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        if (usuario == null || !"SECRETARIO".equals(tipoUsuario)) {
            return "redirect:/home";
        }

        atendimentoRepository.findById(id).ifPresent(atendimento -> {
            atendimento.setCancelado(true);
            atendimentoRepository.save(atendimento);
        });

        return "redirect:/home";
    }

    @GetMapping("/detalhes/{id}")
    public ModelAndView detalhesAtendimento(@PathVariable Long id, HttpSession session, ModelAndView model) {
        Object usuario = session.getAttribute("usuario");
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        if (usuario == null || !"SECRETARIO".equals(tipoUsuario)) {
            model.setViewName("redirect:/auth");
            return model;
        }

        Atendimento atendimento = atendimentoRepository.findById(id).orElse(null);
        model.addObject("atendimento", atendimento);
        model.setViewName("detalhesagendamento"); // novo template Thymeleaf
        return model;
    }
}
