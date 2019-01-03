package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/22/2015.
 */
@Transactional
public interface TipoServicioRepository extends JpaRepository<TipoServicio,Long>{
}
