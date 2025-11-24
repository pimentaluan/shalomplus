package br.ifpb.shalomplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ifpb.shalomplus.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Aluno findByMatricula(String matricula);
    boolean existsByMatricula(String matricula);
}
