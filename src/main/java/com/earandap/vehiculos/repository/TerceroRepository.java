package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Tercero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Angel Luis on 10/26/2015.
 */
@Transactional
public interface TerceroRepository extends JpaRepository<Tercero,Long> {

    @Query(value = "SELECT * from tercero t FULL JOIN persona p ON t.tercero_id=p.tercero_id FULL JOIN empresa e on t.tercero_id=e.tercero_id WHERE (LOWER(CONCAT(p.persona_primernombre,' ',p.persona_segundonombre,' ',p.persona_primerapellido,' ',p.persona_segundoapellido)) LIKE LOWER(CONCAT ('%',:query, '%'))) OR (LOWER(e.empresa_razonsocial) LIKE LOWER(CONCAT ('%',:query, '%')))", nativeQuery = true)
    List<Tercero> search(@Param("query") String query);

}
