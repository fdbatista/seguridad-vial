package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Infraccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Transactional
public interface InfraccionRepository extends JpaRepository<Infraccion, Long> {
}
