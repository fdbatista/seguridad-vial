package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/13/2015.
 */
@Transactional
public interface RecursoRespository extends JpaRepository<Recurso,Long> {

    Recurso findByCodigo(String recurso);
}
