package com.clinica.api.dto;

import com.clinica.api.model.Paciente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Dados do paciente")
public record PacienteDTO(

        @Schema(description = "ID do paciente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome completo", example = "João da Silva")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Schema(description = "E-mail do paciente", example = "joao.silva@email.com")
        String email,

        @NotBlank(message = "CPF é obrigatório")
        @CPF(message = "CPF inválido")
        @Schema(description = "CPF do paciente", example = "123.456.789-09")
        String cpf,

        @NotBlank(message = "Telefone é obrigatório")
        @Schema(description = "Telefone de contato", example = "(12) 98888-8888")
        String telefone,

        @NotBlank(message = "Endereço é obrigatório")
        @Schema(description = "Endereço completo", example = "Rua das Flores, 100 - São José dos Campos/SP")
        String endereco,

        @Schema(description = "Indica se o paciente está ativo", example = "true")
        Boolean ativo
) {
        public static PacienteDTO fromEntity(Paciente paciente) {
                return new PacienteDTO(
                        paciente.getId(),
                        paciente.getNome(),
                        paciente.getEmail(),
                        paciente.getCpf(),
                        paciente.getTelefone(),
                        paciente.getEndereco(),
                        paciente.getAtivo()
                );
        }

        public Paciente toEntity() {
                return Paciente.builder()
                        .nome(nome)
                        .email(email)
                        .cpf(cpf)
                        .telefone(telefone)
                        .endereco(endereco)
                        .ativo(ativo != null ? ativo : true)
                        .build();
        }
}
