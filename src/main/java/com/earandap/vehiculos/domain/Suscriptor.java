package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Angel Luis on 12/18/2015.
 */
@Entity
@Table(name = "suscriptor")
@AssociationOverrides({
        @AssociationOverride(name = "id.tercero",
                joinColumns = @JoinColumn(name = "tercero_id")),
        @AssociationOverride(name = "id.notificacion",
                joinColumns = @JoinColumn(name = "tiponotificacion_id")) })
public class Suscriptor {

    @EmbeddedId
    private Id id = new Id();

    @Transient
    private Tercero tercero;

    @Transient
    private TipoNotificacion notificacion;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
        this.setNotificacion(id.notificacion);
        this.setTercero(id.tercero);
    }

    public Tercero getTercero() {
        return id.tercero;
    }

    public void setTercero(Tercero tercero) {
        id.tercero = tercero;
        this.tercero = tercero;
    }

    public TipoNotificacion getNotificacion() {
        return id.notificacion;
    }

    public void setNotificacion(TipoNotificacion notificacion) {
        id.setNotificacion(notificacion);
        this.notificacion = notificacion;
    }

    @Embeddable
    public static class Id implements Serializable {

        @ManyToOne
        private Tercero tercero;

        @ManyToOne
        private TipoNotificacion notificacion;

        public Tercero getTercero() {
            return tercero;
        }

        public void setTercero(Tercero tercero) {
            this.tercero = tercero;
        }

        public TipoNotificacion getNotificacion() {
            return notificacion;
        }

        public void setNotificacion(TipoNotificacion notificacion) {
            this.notificacion = notificacion;
        }

        public Id() {
        }

        public Id(TipoNotificacion notificacion, Tercero tercero) {
            this.notificacion = notificacion;
            this.tercero = tercero;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;

            Id id = (Id) o;

            if (!tercero.equals(id.tercero)) return false;
            return notificacion.equals(id.notificacion);

        }

        @Override
        public int hashCode() {
            int result = tercero.hashCode();
            result = 31 * result + notificacion.hashCode();
            return result;
        }
    }

    @Override
    public String toString() {
        return tercero.getNombreTercero();
    }
}
