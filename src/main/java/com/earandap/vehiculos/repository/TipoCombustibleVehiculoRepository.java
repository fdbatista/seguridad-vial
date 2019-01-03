package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoCombustible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author felix.batista
 */
@Transactional
public interface TipoCombustibleVehiculoRepository extends JpaRepository<TipoCombustible, Long> {
}
