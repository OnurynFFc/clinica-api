package com.clinica.api.repository;

import com.clinica.api.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable pageable);

    Page<Medico> findAllByEspecialidadeAndAtivoTrue(Medico.Especialidade especialidade, Pageable pageable);

    Optional<Medico> findByCrm(String crm);

    boolean existsByEmail(String email);

    boolean existsByCrm(String crm);
}
