package com.clinica.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String crm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especialidade especialidade;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private Boolean ativo = true;

    public enum Especialidade {
        CARDIOLOGIA,
        DERMATOLOGIA,
        ORTOPEDIA,
        PEDIATRIA,
        PSIQUIATRIA,
        GINECOLOGIA,
        NEUROLOGIA,
        CLINICO_GERAL
    }
}
