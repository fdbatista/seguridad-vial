package com.earandap.vehiculos.service;

import com.earandap.vehiculos.domain.Notificacion;
import com.earandap.vehiculos.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by angel on 31/03/16.
 */
@Service
public class NotificationServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> getNotificacionesAProcesar(Date date) {
        return notificacionRepository.findByFechaNotificacionAndProcesada(date, false);
    }

    public void actualizarNotificacionesProcesadas(List<Notificacion> notificaciones) {
        for (Notificacion notificacion : notificaciones) {
            notificacion.setProcesada(true);
            notificacionRepository.save(notificacion);
        }
    }
}
