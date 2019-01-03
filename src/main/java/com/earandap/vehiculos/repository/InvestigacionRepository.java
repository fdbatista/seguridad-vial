package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Investigacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/30/2015.
 */
@Transactional
public interface InvestigacionRepository extends JpaRepository<Investigacion, Long>{
}
