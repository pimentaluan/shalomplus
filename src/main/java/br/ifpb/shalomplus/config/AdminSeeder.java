package br.ifpb.shalomplus.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.ifpb.shalomplus.model.Administrador;
import br.ifpb.shalomplus.repository.AdministradorRepository;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdministradorRepository administradorRepository;

    public AdminSeeder(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public void run(String... args) {
        String loginPadrao = "admin";

        // só cria se ainda não existir
        if (administradorRepository.findByLogin(loginPadrao) == null) {
            Administrador admin = new Administrador();
            admin.setNome("Administrador");
            admin.setLogin(loginPadrao);
            admin.setSenha("admin123");

            administradorRepository.save(admin);

            System.out.println("✅ Admin padrão criado: login=admin senha=admin123");
        }
    }
}
