package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.MantenimientoActividad;
import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import com.earandap.vehiculos.domain.nomenclador.TipoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Angel Luis on 10/24/2015.
 */
@Transactional
public interface MantenimientoActividadRepository extends JpaRepository<MantenimientoActividad, MantenimientoActividad.Id> {

    @Query("SELECT m.id.actividad FROM MantenimientoActividad m WHERE m.id.claseVehiculo =:claseVehiculo and m.id.tipoMantenimiento = :tipoMantenimiento and m.id.actividad.id not in :ids")
    List<Actividad> buscarActividadesInMantenimientoActividad(@Param("claseVehiculo") ClaseVehiculo claseVehiculo, @Param("tipoMantenimiento") TipoMantenimiento tipoMantenimiento, @Param("ids") Set<Long> ids);

    @Query("SELECT m.id.actividad FROM MantenimientoActividad m WHERE m.id.claseVehiculo =:claseVehiculo and m.id.tipoMantenimiento = :tipoMantenimiento")
    List<Actividad> buscarActividadesInMantenimientoActividad(@Param("claseVehiculo") ClaseVehiculo tipoVehiculo, @Param("tipoMantenimiento") TipoMantenimiento tipoMantenimiento);

}
