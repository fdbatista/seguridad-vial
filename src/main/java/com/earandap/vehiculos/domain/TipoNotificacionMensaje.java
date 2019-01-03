package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Angel Luis on 12/18/2015.
 */
@Entity
@Table(name = "tipo_notificacion_mensaje")
@AssociationOverrides({
        @AssociationOverride(name = "id.tipoNotificacion",
                joinColumns = @JoinColumn(name = "tiponotificacion_id")),
        @AssociationOverride(name = "id.mensaje",
                joinColumns = @JoinColumn(name = "tipomensaje_id")) })
public class TipoNotificacionMensaje {

    @Transient
    private TipoNotificacion tipoNotificacion;

    @Transient
    private TipoMensaje tipoMensaje;

    @Column(name = "activo")
    private boolean activo;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public TipoNotificacion getTipoNotificacion() {
        return id.tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
        id.setTipoNotificacion(tipoNotificacion);
    }

    public TipoMensaje getTipoMensaje() {
        return id.tipoMensaje;
    }

    public void setTipoMensaje(TipoMensaje tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
        id.setTipoMensaje(tipoMensaje);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @EmbeddedId
    private Id id = new Id();

    @Embeddable
    public static class Id implements Serializable {

        @ManyToOne
        private TipoNotificacion tipoNotificacion;

        @ManyToOne
        private TipoMensaje tipoMensaje;

        public TipoNotificacion getTipoNotificacion() {
            return tipoNotificacion;
        }

        public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
            this.tipoNotificacion = tipoNotificacion;
        }

        public TipoMensaje getTipoMensaje() {
            return tipoMensaje;
        }

        public void setTipoMensaje(TipoMensaje tipoMensaje) {
            this.tipoMensaje = tipoMensaje;
        }

        public Id() {
        }

        public Id(TipoNotificacion tipoNotificacion, TipoMensaje tipoMensaje) {
            this.tipoNotificacion = tipoNotificacion;
            this.tipoMensaje = tipoMensaje;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;

            Id id = (Id) o;

            if (!tipoNotificacion.equals(id.tipoNotificacion)) return false;
            return tipoMensaje.equals(id.tipoMensaje);

        }

        @Override
        public int hashCode() {
            int result = tipoNotificacion.hashCode();
            result = 31 * result + tipoMensaje.hashCode();
            return result;
        }
    }

}
