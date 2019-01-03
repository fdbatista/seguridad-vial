package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Sexo;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/8/2015.
 */
@Transactional
public interface SexoRepository extends NomencladorBaseRepository<Sexo>{
}
