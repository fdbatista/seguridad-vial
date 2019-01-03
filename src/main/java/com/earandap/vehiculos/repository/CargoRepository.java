package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/14/2015.
 */
@Transactional
public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
