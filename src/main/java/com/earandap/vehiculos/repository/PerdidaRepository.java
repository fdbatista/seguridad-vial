package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Perdida;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Transactional
public interface PerdidaRepository extends NomencladorBaseRepository<Perdida> {
}
