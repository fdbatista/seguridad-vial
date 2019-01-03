package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Transactional
public interface SucursalRepository extends JpaRepository<Sucursal,Long> {

    //@Query("SELECT s FROM Sucursal s WHERE s.documento = :documento")
    //Sucursal searchSucursal(@Param("documento") String documento);

    @Query("SELECT s FROM Sucursal s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT ('%',:query, '%'))")
    List<Sucursal> search(@Param("query") String query);
}
