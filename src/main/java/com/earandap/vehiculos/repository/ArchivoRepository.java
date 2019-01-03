package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Archivo;
import com.earandap.vehiculos.domain.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by eduardo on 13-Oct-15.
 */
@Transactional
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {

    @Modifying
    @Query("UPDATE Archivo a SET a.incidente = ?1 WHERE a.id IN ?2")
    public void setIncidente(Incidente incidente, List<Long> ids);
}
