package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoActividad;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/21/2015.
 */
@Transactional
public interface TipoActividadRepository extends NomencladorBaseRepository<TipoActividad> {
}
