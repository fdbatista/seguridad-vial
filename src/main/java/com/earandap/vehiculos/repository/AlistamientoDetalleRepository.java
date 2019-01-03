package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.AlistamientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/4/2015.
 */
@Transactional
public interface AlistamientoDetalleRepository extends JpaRepository<AlistamientoDetalle,Long>{
}
