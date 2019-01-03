package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/29/2015.
 */
@Transactional
public interface ContratoRepository extends JpaRepository<Contrato, Long>{
}
