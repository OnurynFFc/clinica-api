package com.clinica.api.repository;

import com.clinica.api.model.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findAllByStatus(Consulta.StatusConsulta status, Pageable pageable);

    Page<Consulta> findAllByMedicoId(Long medicoId, Pageable pageable);

    Page<Consulta> findAllByPacienteId(Long pacienteId, Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.medico.id = :medicoId " +
           "AND c.dataHora = :dataHora AND c.status = 'AGENDADA'")
    boolean existsConflitoMedico(@Param("medicoId") Long medicoId,
                                 @Param("dataHora") LocalDateTime dataHora);

    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.paciente.id = :pacienteId " +
           "AND c.dataHora = :dataHora AND c.status = 'AGENDADA'")
    boolean existsConflitoPaciente(@Param("pacienteId") Long pacienteId,
                                   @Param("dataHora") LocalDateTime dataHora);
}
