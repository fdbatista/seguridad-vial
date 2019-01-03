package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Seguro;
import com.earandap.vehiculos.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Angel Luis on 10/18/2015.
 */
@Transactional
public interface SeguroRepository extends JpaRepository<Seguro,Long>{

    @Modifying
    @Query("UPDATE Seguro a SET a.vehiculo = ?1 WHERE a.id IN ?2")
    public void setVehiculo(Vehiculo vehiculo, List<Long> ids);
    
    @Query(value = "select * from seguro where seguro_fechavencimiento - current_date = (select cast(param_valor as integer) from parametros where param_id = 1)", nativeQuery = true)
    public List<Seguro> getSegurosProximoVencimiento();
}
