package com.clinica.api.controller;

import com.clinica.api.dto.ConsultaDTO;
import com.clinica.api.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Endpoints para agendamento e gestão de consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    @Operation(summary = "Agendar consulta", description = "Agenda uma nova consulta aplicando regras de negócio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Médico ou paciente não encontrado"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (horário, conflito, etc.)")
    })
    public ResponseEntity<ConsultaDTO> agendar(@RequestBody @Valid ConsultaDTO dto) {
        ConsultaDTO criada = consultaService.agendar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(uri).body(criada);
    }

    @GetMapping
    @Operation(summary = "Listar consultas", description = "Lista todas as consultas com paginação")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<Page<ConsultaDTO>> listar(
            @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return ResponseEntity.ok(consultaService.listar(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping("/medico/{medicoId}")
    @Operation(summary = "Listar consultas por médico")
    public ResponseEntity<Page<ConsultaDTO>> listarPorMedico(
            @PathVariable Long medicoId,
            @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return ResponseEntity.ok(consultaService.listarPorMedico(medicoId, pageable));
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar consultas por paciente")
    public ResponseEntity<Page<ConsultaDTO>> listarPorPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId, pageable));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar consulta", description = "Cancela uma consulta com mínimo 24h de antecedência")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta cancelada"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
            @ApiResponse(responseCode = "422", description = "Consulta não pode ser cancelada")
    })
    public ResponseEntity<ConsultaDTO> cancelar(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String motivo = body.getOrDefault("motivoCancelamento", "Cancelado pelo solicitante");
        return ResponseEntity.ok(consultaService.cancelar(id, motivo));
    }
}
