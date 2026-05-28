package com.clinica.api.service;

import com.clinica.api.dto.ConsultaDTO;
import com.clinica.api.exception.BusinessException;
import com.clinica.api.exception.ResourceNotFoundException;
import com.clinica.api.model.Consulta;
import com.clinica.api.model.Medico;
import com.clinica.api.model.Paciente;
import com.clinica.api.repository.ConsultaRepository;
import com.clinica.api.repository.MedicoRepository;
import com.clinica.api.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public ConsultaDTO agendar(ConsultaDTO dto) {
        Medico medico = medicoRepository.findById(dto.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado com id: " + dto.medicoId()));

        if (!medico.getAtivo()) {
            throw new BusinessException("Não é possível agendar consulta com médico inativo.");
        }

        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + dto.pacienteId()));

        if (!paciente.getAtivo()) {
            throw new BusinessException("Não é possível agendar consulta para paciente inativo.");
        }

        validarHorario(dto.dataHora());

        if (consultaRepository.existsConflitoMedico(dto.medicoId(), dto.dataHora())) {
            throw new BusinessException("O médico já possui uma consulta agendada neste horário.");
        }

        if (consultaRepository.existsConflitoPaciente(dto.pacienteId(), dto.dataHora())) {
            throw new BusinessException("O paciente já possui uma consulta agendada neste horário.");
        }

        Consulta consulta = Consulta.builder()
                .medico(medico)
                .paciente(paciente)
                .dataHora(dto.dataHora())
                .status(Consulta.StatusConsulta.AGENDADA)
                .build();

        return ConsultaDTO.fromEntity(consultaRepository.save(consulta));
    }

    @Transactional(readOnly = true)
    public Page<ConsultaDTO> listar(Pageable pageable) {
        return consultaRepository.findAll(pageable).map(ConsultaDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public ConsultaDTO buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(ConsultaDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ConsultaDTO> listarPorMedico(Long medicoId, Pageable pageable) {
        return consultaRepository.findAllByMedicoId(medicoId, pageable).map(ConsultaDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ConsultaDTO> listarPorPaciente(Long pacienteId, Pageable pageable) {
        return consultaRepository.findAllByPacienteId(pacienteId, pageable).map(ConsultaDTO::fromEntity);
    }

    @Transactional
    public ConsultaDTO cancelar(Long id, String motivoCancelamento) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com id: " + id));

        if (consulta.getStatus() != Consulta.StatusConsulta.AGENDADA) {
            throw new BusinessException("Apenas consultas com status AGENDADA podem ser canceladas.");
        }

        if (LocalDateTime.now().plusHours(24).isAfter(consulta.getDataHora())) {
            throw new BusinessException("Consultas só podem ser canceladas com no mínimo 24h de antecedência.");
        }

        consulta.setStatus(Consulta.StatusConsulta.CANCELADA);
        consulta.setMotivoCancelamento(motivoCancelamento);

        return ConsultaDTO.fromEntity(consultaRepository.save(consulta));
    }

    private void validarHorario(LocalDateTime dataHora) {
        if (dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new BusinessException("A clínica não funciona aos domingos.");
        }

        int hora = dataHora.getHour();
        if (hora < 7 || hora >= 18) {
            throw new BusinessException("A clínica funciona apenas entre 07h e 18h.");
        }

        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new BusinessException("Consultas devem ser agendadas com no mínimo 30 minutos de antecedência.");
        }
    }
}
