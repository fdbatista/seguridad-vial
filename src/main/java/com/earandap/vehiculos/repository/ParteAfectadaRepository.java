package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.LineaVehiculo;
import com.earandap.vehiculos.domain.nomenclador.MarcaVehiculo;
import com.earandap.vehiculos.domain.nomenclador.PartesAfectada;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Transactional
public interface ParteAfectadaRepository extends NomencladorBaseRepository<PartesAfectada> {
}
