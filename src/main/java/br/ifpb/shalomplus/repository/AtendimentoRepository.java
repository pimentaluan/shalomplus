package br.ifpb.shalomplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import br.ifpb.shalomplus.model.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    List<Atendimento> findByProfissional_Id(Long profissionalId);
    List<Atendimento> findByAluno_Id(Long alunoId);
    List<Atendimento> findByProfissional_IdAndCanceladoFalseAndDataHoraGreaterThanEqualOrderByDataHoraAsc(
        Long profissionalId, LocalDateTime agora);

    List<Atendimento> findByProfissional_IdAndDataHoraLessThanOrderByDataHoraDesc(
        Long profissionalId, LocalDateTime agora);

    List<Atendimento> findByAluno_IdAndCanceladoFalseAndDataHoraGreaterThanEqualOrderByDataHoraAsc(
        Long alunoId, LocalDateTime agora);

    List<Atendimento> findByAluno_IdAndDataHoraLessThanOrderByDataHoraDesc(
        Long alunoId, LocalDateTime agora);

    List<Atendimento> findAllByOrderByDataHoraDesc();


    boolean existsByProfissional_IdAndDataHoraAndCanceladoFalse(Long profissionalId, LocalDateTime dataHora);
}
