package com.clinica.api.dto;

import com.clinica.api.model.Consulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Dados da consulta")
public record ConsultaDTO(

        @Schema(description = "ID da consulta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @NotNull(message = "ID do médico é obrigatório")
        @Schema(description = "ID do médico responsável", example = "1")
        Long medicoId,

        @NotNull(message = "ID do paciente é obrigatório")
        @Schema(description = "ID do paciente", example = "1")
        Long pacienteId,

        @NotNull(message = "Data e hora são obrigatórias")
        @Future(message = "A consulta deve ser agendada para uma data futura")
        @Schema(description = "Data e hora da consulta", example = "2025-12-01T10:00:00")
        LocalDateTime dataHora,

        @Schema(description = "Status da consulta", example = "AGENDADA", accessMode = Schema.AccessMode.READ_ONLY)
        Consulta.StatusConsulta status,

        @Schema(description = "Nome do médico", accessMode = Schema.AccessMode.READ_ONLY)
        String nomeMedico,

        @Schema(description = "Nome do paciente", accessMode = Schema.AccessMode.READ_ONLY)
        String nomePaciente,

        @Schema(description = "Motivo do cancelamento (quando aplicável)")
        String motivoCancelamento
) {
        public static ConsultaDTO fromEntity(Consulta consulta) {
                return new ConsultaDTO(
                        consulta.getId(),
                        consulta.getMedico().getId(),
                        consulta.getPaciente().getId(),
                        consulta.getDataHora(),
                        consulta.getStatus(),
                        consulta.getMedico().getNome(),
                        consulta.getPaciente().getNome(),
                        consulta.getMotivoCancelamento()
                );
        }
}
