package com.clinica.api.dto;

import com.clinica.api.model.Medico;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados do médico")
public record MedicoDTO(

        @Schema(description = "ID do médico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome completo", example = "Dr. Carlos Silva")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Schema(description = "E-mail do médico", example = "carlos.silva@clinica.com")
        String email,

        @NotBlank(message = "CRM é obrigatório")
        @Pattern(regexp = "\\d{4,6}", message = "CRM deve conter entre 4 e 6 dígitos")
        @Schema(description = "Número do CRM", example = "123456")
        String crm,

        @NotNull(message = "Especialidade é obrigatória")
        @Schema(description = "Especialidade médica", example = "CARDIOLOGIA")
        Medico.Especialidade especialidade,

        @NotBlank(message = "Telefone é obrigatório")
        @Schema(description = "Telefone de contato", example = "(12) 99999-9999")
        String telefone,

        @Schema(description = "Indica se o médico está ativo", example = "true")
        Boolean ativo
) {
        public static MedicoDTO fromEntity(Medico medico) {
                return new MedicoDTO(
                        medico.getId(),
                        medico.getNome(),
                        medico.getEmail(),
                        medico.getCrm(),
                        medico.getEspecialidade(),
                        medico.getTelefone(),
                        medico.getAtivo()
                );
        }

        public Medico toEntity() {
                return Medico.builder()
                        .nome(nome)
                        .email(email)
                        .crm(crm)
                        .especialidade(especialidade)
                        .telefone(telefone)
                        .ativo(ativo != null ? ativo : true)
                        .build();
        }
}
