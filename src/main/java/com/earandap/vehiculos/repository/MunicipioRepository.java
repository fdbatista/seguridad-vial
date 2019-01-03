package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Municipio;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/9/2015.
 */
@Transactional
public interface MunicipioRepository extends NomencladorBaseRepository<Municipio> {
}
