package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.TipoNotificacionMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 12/26/2015.
 */
@Transactional
public interface TipoMensajeNotificacionRepository extends JpaRepository<TipoNotificacionMensaje,TipoNotificacionMensaje.Id> {
}
