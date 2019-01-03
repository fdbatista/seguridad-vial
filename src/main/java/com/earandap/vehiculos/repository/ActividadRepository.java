package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by Angel Luis on 10/24/2015.
 */
@Transactional
public interface ActividadRepository extends JpaRepository<Actividad,Long> {

    List<Actividad> findByIdNotIn(Collection<Long> excludeIds);
}
