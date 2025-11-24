package br.ifpb.shalomplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ifpb.shalomplus.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Profissional findByCr(String cr);
    boolean existsByCr(String cr);
}
