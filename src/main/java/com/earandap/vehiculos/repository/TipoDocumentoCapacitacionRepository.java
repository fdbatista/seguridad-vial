package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoDocumentoCapacitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by felix.batista.
 */
@Transactional
public interface TipoDocumentoCapacitacionRepository extends JpaRepository<TipoDocumentoCapacitacion, Long> {
}
