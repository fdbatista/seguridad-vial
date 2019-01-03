package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoDocumentoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by felix.batista.
 */
@Transactional
public interface TipoDocumentoEmpresaRepository extends JpaRepository<TipoDocumentoEmpresa, Long> {
}
