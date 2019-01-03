package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.MantenimientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/30/2015.
 */
@Transactional
public interface MantenimientoDetalleRepository extends JpaRepository<MantenimientoDetalle, Long> {
}
