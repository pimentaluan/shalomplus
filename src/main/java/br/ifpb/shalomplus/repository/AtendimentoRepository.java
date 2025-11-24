package br.ifpb.shalomplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import br.ifpb.shalomplus.model.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    List<Atendimento> findByProfissional_Id(Long profissionalId);
    List<Atendimento> findByAluno_Id(Long alunoId);
}
