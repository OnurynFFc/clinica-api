package com.clinica.api.controller;

import com.clinica.api.dto.MedicoDTO;
import com.clinica.api.model.Medico;
import com.clinica.api.service.MedicoService;
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

@RestController
@RequestMapping("/api/v1/medicos")
@RequiredArgsConstructor
@Tag(name = "Médicos", description = "Endpoints para gerenciamento de médicos")
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping
    @Operation(summary = "Cadastrar médico", description = "Cadastra um novo médico no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "E-mail ou CRM já cadastrado")
    })
    public ResponseEntity<MedicoDTO> cadastrar(@RequestBody @Valid MedicoDTO dto) {
        MedicoDTO criado = medicoService.cadastrar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar médicos", description = "Lista todos os médicos ativos com paginação")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<Page<MedicoDTO>> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(medicoService.listar(pageable));
    }

    @GetMapping("/especialidade/{especialidade}")
    @Operation(summary = "Listar por especialidade", description = "Lista médicos ativos por especialidade")
    public ResponseEntity<Page<MedicoDTO>> listarPorEspecialidade(
            @PathVariable Medico.Especialidade especialidade,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(medicoService.listarPorEspecialidade(especialidade, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar médico por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico encontrado"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<MedicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar médico", description = "Atualiza dados do médico (nome, telefone, especialidade)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico atualizado"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<MedicoDTO> atualizar(@PathVariable Long id,
                                               @RequestBody MedicoDTO dto) {
        return ResponseEntity.ok(medicoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar médico", description = "Realiza exclusão lógica (soft delete) do médico")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Médico inativado"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        medicoService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
