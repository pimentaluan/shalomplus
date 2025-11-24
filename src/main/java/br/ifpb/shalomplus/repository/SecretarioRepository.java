package br.ifpb.shalomplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ifpb.shalomplus.model.Secretario;

public interface SecretarioRepository extends JpaRepository<Secretario, Long> {
    Secretario findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
