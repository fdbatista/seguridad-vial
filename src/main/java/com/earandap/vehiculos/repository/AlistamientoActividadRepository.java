package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.AlistamientoActividad;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Angel Luis on 11/2/2015.
 */
@Transactional
public interface AlistamientoActividadRepository extends JpaRepository<AlistamientoActividad,AlistamientoActividad.Id> {

    @Query("SELECT a.id.actividad FROM AlistamientoActividad a WHERE a.id.claseVehiculo =:claseVehiculo and a.id.actividad.id not in :ids")
    List<Actividad> buscarActividadesInAlistamientoActividadExcludeId(@Param("claseVehiculo") ClaseVehiculo claseVehiculo, @Param("ids") Set<Long> ids);

    @Query("SELECT a.id.actividad FROM AlistamientoActividad a WHERE a.id.claseVehiculo =:claseVehiculo")
    List<Actividad> buscarActividadesInAlistamientoActividad(@Param("claseVehiculo") ClaseVehiculo claseVehiculo);
}
