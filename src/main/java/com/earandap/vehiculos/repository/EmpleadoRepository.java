package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 1/13/2016.
 */
@Transactional
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    @Query("SELECT count(e.id) FROM Empleado e")
    int getEmployeesCount();
    
}
