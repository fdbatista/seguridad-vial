package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Suscriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 1/2/2016.
 */
@Transactional
public interface SuscriptorRepository extends JpaRepository<Suscriptor, Suscriptor.Id> {
    
    /*@Query(value = "SELECT s from Suscriptor s where s.notificacion = :notificacion")
    List<Suscriptor> getByTipoNotificacion(@Param("notificacion") TipoNotificacion tipoNotificacion);*/
    
}
