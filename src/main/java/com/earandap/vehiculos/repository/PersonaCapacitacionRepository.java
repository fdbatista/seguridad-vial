package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.PersonaCapacitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/21/2015.
 */
@Transactional
public interface PersonaCapacitacionRepository extends JpaRepository<PersonaCapacitacion,PersonaCapacitacion.Id>{
}
