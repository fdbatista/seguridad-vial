package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Angel Luis on 12/24/2015.
 */
@Transactional
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByFechaNotificacionAndProcesada(Date fechaNotificacion, boolean procesada);
    
    @Query("SELECT n FROM Notificacion n WHERE n.fechaNotificacion < :fecha and procesada = false")
    List<Notificacion> findPendientes(@Param("fecha") Date fecha/*, @Param("procesada") boolean procesada*/);
    
    @Query("SELECT n FROM Notificacion n WHERE n.detalle = :detalle")
    Notificacion existe(@Param("detalle") String detalle);
    
    @Query(value = "select * from notificacion n where notificacion_fechanotificacion <= current_date and notificacion_procesada = false", nativeQuery = true)
    List<Notificacion> findPendientes();
    
    //nativeQuery = true
    
    

}
