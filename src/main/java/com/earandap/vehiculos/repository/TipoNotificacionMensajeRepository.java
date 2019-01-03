package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.TipoNotificacionMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 12/24/2015.
 */
@Transactional
public interface TipoNotificacionMensajeRepository extends JpaRepository<TipoNotificacionMensaje, Long> {
}
