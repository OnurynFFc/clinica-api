package com.clinica.api.service;

import com.clinica.api.dto.PacienteDTO;
import com.clinica.api.exception.BusinessException;
import com.clinica.api.exception.ResourceNotFoundException;
import com.clinica.api.model.Paciente;
import com.clinica.api.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional
    public PacienteDTO cadastrar(PacienteDTO dto) {
        if (pacienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Já existe um paciente cadastrado com este e-mail.");
        }
        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new BusinessException("Já existe um paciente cadastrado com este CPF.");
        }
        Paciente paciente = pacienteRepository.save(dto.toEntity());
        return PacienteDTO.fromEntity(paciente);
    }

    @Transactional(readOnly = true)
    public Page<PacienteDTO> listar(Pageable pageable) {
        return pacienteRepository.findAllByAtivoTrue(pageable)
                .map(PacienteDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public PacienteDTO buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(PacienteDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));
    }

    @Transactional
    public PacienteDTO atualizar(Long id, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));

        if (dto.nome() != null) paciente.setNome(dto.nome());
        if (dto.telefone() != null) paciente.setTelefone(dto.telefone());
        if (dto.endereco() != null) paciente.setEndereco(dto.endereco());

        return PacienteDTO.fromEntity(pacienteRepository.save(paciente));
    }

    @Transactional
    public void inativar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);
    }
}
