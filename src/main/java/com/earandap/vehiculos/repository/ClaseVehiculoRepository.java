package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/23/2015.
 */
@Transactional
public interface ClaseVehiculoRepository extends JpaRepository<ClaseVehiculo,Long> {
}
