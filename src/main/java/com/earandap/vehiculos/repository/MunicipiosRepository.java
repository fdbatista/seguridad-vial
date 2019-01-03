package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Municipios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by angel on 06/04/16.
 */
@Transactional
public interface MunicipiosRepository extends JpaRepository<Municipios,Long> {

    @Query("SELECT m FROM Municipios m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT ('%',:query, '%'))")
    List<Municipios> search(@Param("query") String query);
}
