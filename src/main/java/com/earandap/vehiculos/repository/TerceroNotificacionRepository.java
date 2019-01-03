package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.TerceroNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by angel on 12/04/16.
 */
@Transactional
public interface TerceroNotificacionRepository extends JpaRepository<TerceroNotificacion, TerceroNotificacion.Id> {

    @Query("SELECT count(*) FROM TerceroNotificacion t WHERE t.id.tercero = :persona")
    int countNotificacionesByPerson(@Param("persona") Persona persona);

    @Query("SELECT t FROM TerceroNotificacion t WHERE t.id.tercero = :persona")
    List<TerceroNotificacion> notificacionesByPerson(@Param("persona") Persona persona);
    
    @Query(value = "select * from tercero_notificacion where tercero_id = :id_persona and leido = false order by notificacion_id desc limit 15", nativeQuery = true)
    List<TerceroNotificacion> ultimasNotificacionesByPerson(@Param("id_persona") Long id_persona);

    @Query("SELECT count(*) FROM TerceroNotificacion t WHERE t.id.tercero = :persona and t.leido is false")
    int countNotificacionesNoLeidoByPerson(@Param("persona") Persona persona);

    @Query("SELECT t FROM TerceroNotificacion t WHERE t.id.tercero = :persona and t.leido is false")
    List<TerceroNotificacion> notificacionesNoLeidasByPerson(@Param("persona") Persona persona);
    
}
