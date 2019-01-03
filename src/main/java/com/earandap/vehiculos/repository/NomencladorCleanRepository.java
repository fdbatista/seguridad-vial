package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.Nomenclador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Transactional
public interface NomencladorCleanRepository extends JpaRepository<Nomenclador, Long> {
    
    @Query("UPDATE Nomenclador n SET n.tipo = :tipo where n.id = :id")
    List<Actividad> guardar(@Param("tipo") String tipo, @Param("id") Long id);
}
