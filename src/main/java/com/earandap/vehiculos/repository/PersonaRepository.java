package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Angel Luis on 10/3/2015.
 */
@Transactional
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT p FROM Persona p WHERE LOWER(CONCAT(p.primerNombre, ' ', p.primerApellido, case when p.segundoNombre != null then concat(' ', p.segundoNombre) else '' end, case when p.segundoApellido != null then concat(' ', p.segundoApellido) else '' end)) LIKE LOWER(CONCAT ('%',:query, '%')) OR p.documento = :query")
    List<Persona> search(@Param("query") String query);

    @Query("SELECT p FROM Persona p WHERE p.tipoDocumento =:tipoDocumento and p.documento = :documento")
    Persona searchPersona(@Param("tipoDocumento") TipoDocumento tipoDocumento, @Param("documento") String documento);

    List<Persona> findByConductorIsNotNull();

    List<Persona> findByEmpleadoNotNull();

    Persona findByInactivoFalse();

    @Query("SELECT p FROM Persona p WHERE (LOWER(CONCAT(p.primerNombre, ' ', p.primerApellido, case when p.segundoNombre != null then concat(' ', p.segundoNombre) else '' end, case when p.segundoApellido != null then concat(' ', p.segundoApellido) else '' end)) LIKE LOWER(CONCAT ('%',:query, '%')) OR p.documento = :query) AND p.conductor IS NOT NULL")
    List<Persona> searchConductor(@Param("query") String s);
}
