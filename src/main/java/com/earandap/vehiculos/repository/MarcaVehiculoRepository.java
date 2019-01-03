package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.MarcaVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author felix.batista
 */
@Transactional
public interface MarcaVehiculoRepository extends JpaRepository<MarcaVehiculo, Long> {
}
