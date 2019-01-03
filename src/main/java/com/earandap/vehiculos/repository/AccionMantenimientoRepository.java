package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.AccionMantenimiento;
import com.earandap.vehiculos.domain.Tercero;
import com.earandap.vehiculos.domain.Vehiculo;
import com.earandap.vehiculos.domain.nomenclador.TipoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/26/2015.
 */
@Transactional
public interface AccionMantenimientoRepository extends JpaRepository<AccionMantenimiento,Long> {
    //@Query("SELECT p FROM Persona p WHERE p.tipoDocumento =:tipoDocumento and p.documento = :documento")
    AccionMantenimiento findOneByTipoMantenimientoAndVehiculoAndTecnico(TipoMantenimiento tipoMantenimiento,Vehiculo vehiculo, Tercero tecnico);
}
