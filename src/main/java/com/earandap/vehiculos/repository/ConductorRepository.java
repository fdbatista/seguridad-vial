package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Angel Luis on 10/7/2015.
 */
@Transactional
public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    //@Query("SELECT c FROM Conductor c WHERE LOWER(c.persona.primerNombre) = LOWER(:query) OR c.persona.documento = :query")
    @Query("SELECT c FROM Conductor c WHERE LOWER(CONCAT(c.persona.primerNombre, ' ', c.persona.primerApellido, case when c.persona.segundoNombre != null then concat(' ', c.persona.segundoNombre) else '' end, case when c.persona.segundoApellido != null then concat(' ', c.persona.segundoApellido) else '' end)) LIKE LOWER(CONCAT ('%',:query, '%')) OR c.persona.documento = :query")
    List<Conductor> search(@Param("query") String query);
}
