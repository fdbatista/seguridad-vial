package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Capacitacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/19/2015.
 */
@Transactional
public interface CapacitacionRepository extends JpaRepository<Capacitacion, Long> {
    
    @Query(value = "select * from capacitacion where capacitacion_fechavencimiento - current_date = (select cast(param_valor as integer) from parametros where param_id = 1)", nativeQuery = true)
    public List<Capacitacion> getCapacitacionesProximoVencimiento();
    
}
