package com.earandap.vehiculos.service;

import com.earandap.vehiculos.domain.Notificacion;

import java.util.Date;
import java.util.List;

/**
 * Created by angel on 31/03/16.
 */
public interface NotificacionService {

    List<Notificacion> getNotificacionesAProcesar(Date date);

    void actualizarNotificacionesProcesadas(List<Notificacion> notificaciones);

}
