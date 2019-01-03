package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Angel Luis on 12/20/2015.
 */
@Entity
@Table(name = "tercero_notificacion")
@AssociationOverrides({
        @AssociationOverride(name = "id.tercero",
                joinColumns = @JoinColumn(name = "tercero_id")),
        @AssociationOverride(name = "id.notificacion",
                joinColumns = @JoinColumn(name = "notificacion_id")) })
public class TerceroNotificacion {

    @EmbeddedId
    private Id id = new Id();

    @Transient
    private Tercero tercero;

    @Transient
    private Notificacion notificacion;

    @Column(name = "notificado")
    private boolean notificado;

    @Column(name = "leido")
    private boolean leido;

    @Column(name = "activa")
    private boolean activa;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Tercero getTercero() {
        return tercero;
    }

    public void setTercero(Tercero tercero) {
        this.id.tercero = tercero;
        this.tercero = tercero;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Notificacion notificacion) {
        this.id.notificacion = notificacion;
        this.notificacion = notificacion;
    }

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Embeddable
    public static class Id implements Serializable {

        @ManyToOne
        private Tercero tercero;

        @ManyToOne
        private Notificacion notificacion;

        public Id() {
        }

        public Tercero getTercero() {
            return tercero;
        }

        public void setTercero(Tercero tercero) {
            this.tercero = tercero;
        }

        public Notificacion getNotificacion() {
            return notificacion;
        }

        public void setNotificacion(Notificacion notificacion) {
            this.notificacion = notificacion;
        }

        public Id(Tercero tercero, Notificacion notificacion) {
            this.tercero = tercero;
            this.notificacion = notificacion;
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
}
