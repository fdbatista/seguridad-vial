package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Licencia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/22/2015.
 */
@Transactional
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
    
    @Query(value = "select * from licencia where licencia_vigencia - current_date = (select cast(param_valor as integer) from parametros where param_id = 1)", nativeQuery = true)
    public List<Licencia> getLicenciasProximoVencimiento();
    
}
