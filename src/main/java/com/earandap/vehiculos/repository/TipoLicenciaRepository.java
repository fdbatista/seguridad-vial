package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoLicencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/22/2015.
 */
@Transactional
public interface TipoLicenciaRepository extends JpaRepository<TipoLicencia,Long>{
}
