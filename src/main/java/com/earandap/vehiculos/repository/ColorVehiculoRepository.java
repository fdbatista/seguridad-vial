package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Transactional
public interface ColorVehiculoRepository extends JpaRepository<Color, Long> {
}
