package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Nomenclador;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Transactional
public interface NomencladorRepository extends NomencladorBaseRepository<Nomenclador> {

    @Query("SELECT DISTINCT n.tipo FROM Nomenclador n ORDER BY n.tipo")
    List<String> getTiposDistinct();
    
}
