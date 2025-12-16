package br.ifpb.shalomplus.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Aluno aluno;

    @ManyToOne(optional = false)
    private Profissional profissional;

    private LocalDateTime dataHora;

    private String tipo; 
    // exemplos: "Psicológico", "Médico", "Avaliação inicial"

    @Column(length = 1000)
    private String observacoes;

    private Boolean realizado = false;
    private Boolean cancelado = false;
}
