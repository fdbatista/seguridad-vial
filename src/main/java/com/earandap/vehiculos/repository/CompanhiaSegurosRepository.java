package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.CompanhiaSeguros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/17/2015.
 */
@Transactional
public interface CompanhiaSegurosRepository extends JpaRepository<CompanhiaSeguros, Long>{
}
