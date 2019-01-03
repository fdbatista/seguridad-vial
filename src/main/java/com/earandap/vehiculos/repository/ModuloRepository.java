package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/12/2015.
 */
@Transactional
public interface ModuloRepository extends JpaRepository<Modulo,Long>{

}
