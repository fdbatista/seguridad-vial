package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.CapacitacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by felix.batista.
 */
@Transactional
public interface CapacitacionDocumentoRepository extends JpaRepository<CapacitacionDocumento, Long> {
}
