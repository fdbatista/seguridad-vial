package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Conductor;
import com.earandap.vehiculos.domain.Incidente;
import com.earandap.vehiculos.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Transactional
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    @Query("SELECT i FROM Incidente i WHERE i.conductor = :persona and i.vehiculo = :vehiculo")
    List<Incidente> findByConductorAndVehiculo(@Param("persona") Conductor persona, @Param("vehiculo") Vehiculo vehiculo);

}
