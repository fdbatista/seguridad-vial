package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoEvento;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Transactional
public interface TipoEventoRepository extends NomencladorBaseRepository<TipoEvento> {
}
