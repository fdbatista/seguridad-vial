package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.AccidenteInvolucrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 12/3/2015.
 */
@Transactional
public interface AccidenteInvolucradoRepository extends JpaRepository<AccidenteInvolucrado,Long> {
}
