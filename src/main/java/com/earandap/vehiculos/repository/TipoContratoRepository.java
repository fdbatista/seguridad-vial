package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/27/2015.
 */
@Transactional
public interface TipoContratoRepository extends JpaRepository<TipoContrato,Long>{
}
