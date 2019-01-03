package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.AccionAlistamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/2/2015.
 */
@Transactional
public interface AccionAlistamientoRepository extends JpaRepository<AccionAlistamiento,Long> {
}
