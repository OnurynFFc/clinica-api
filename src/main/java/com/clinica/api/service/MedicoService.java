package com.clinica.api.service;

import com.clinica.api.dto.MedicoDTO;
import com.clinica.api.exception.BusinessException;
import com.clinica.api.exception.ResourceNotFoundException;
import com.clinica.api.model.Medico;
import com.clinica.api.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;

    @Transactional
    public MedicoDTO cadastrar(MedicoDTO dto) {
        if (medicoRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Já existe um médico cadastrado com este e-mail.");
        }
        if (medicoRepository.existsByCrm(dto.crm())) {
            throw new BusinessException("Já existe um médico cadastrado com este CRM.");
        }
        Medico medico = medicoRepository.save(dto.toEntity());
        return MedicoDTO.fromEntity(medico);
    }

    @Transactional(readOnly = true)
    public Page<MedicoDTO> listar(Pageable pageable) {
        return medicoRepository.findAllByAtivoTrue(pageable)
                .map(MedicoDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<MedicoDTO> listarPorEspecialidade(Medico.Especialidade especialidade, Pageable pageable) {
        return medicoRepository.findAllByEspecialidadeAndAtivoTrue(especialidade, pageable)
                .map(MedicoDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public MedicoDTO buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .map(MedicoDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com id: " + id));
    }

    @Transactional
    public MedicoDTO atualizar(Long id, MedicoDTO dto) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com id: " + id));

        if (dto.nome() != null) medico.setNome(dto.nome());
        if (dto.telefone() != null) medico.setTelefone(dto.telefone());
        if (dto.especialidade() != null) medico.setEspecialidade(dto.especialidade());

        return MedicoDTO.fromEntity(medicoRepository.save(medico));
    }

    @Transactional
    public void inativar(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com id: " + id));
        medico.setAtivo(false);
        medicoRepository.save(medico);
    }
}
