package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/4/2015.
 */
@Transactional
public interface TipoDocumentoRepository extends NomencladorBaseRepository<TipoDocumento> {
}
