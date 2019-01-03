package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "alistamiento_actividad")
@AssociationOverrides({
        @AssociationOverride(name = "id.claseVehiculo",
                joinColumns = @JoinColumn(name = "clasevehiculo_id")),
        @AssociationOverride(name = "id.actividad",
                joinColumns = @JoinColumn(name = "actividad_id")) })
public class AlistamientoActividad {

    @EmbeddedId
    private Id id = new Id();

    @Transient
    private ClaseVehiculo claseVehiculo;

    @Transient
    private Actividad actividad;

    @Column(name = "alistamientoactividad_inactivo")
    private boolean inactivo;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public ClaseVehiculo getClaseVehiculo() {
        return id.claseVehiculo;
    }

    public void setClaseVehiculo(ClaseVehiculo claseVehiculo) {
        this.claseVehiculo = claseVehiculo;
        id.setClaseVehiculo(claseVehiculo);
    }

    public Actividad getActividad() {
        return id.actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
        id.setActividad(actividad);
    }

    public boolean isInactivo() {
        return inactivo;
    }

    public void setInactivo(boolean inactivo) {
        this.inactivo = inactivo;
    }

    @Embeddable
    public static class Id implements Serializable {

        @ManyToOne
        private ClaseVehiculo claseVehiculo;

        @ManyToOne
        private Actividad actividad;

        public ClaseVehiculo getClaseVehiculo() {
            return claseVehiculo;
        }

        public void setClaseVehiculo(ClaseVehiculo claseVehiculo) {
            this.claseVehiculo = claseVehiculo;
        }

        public Actividad getActividad() {
            return actividad;
        }

        public void setActividad(Actividad actividad) {
            this.actividad = actividad;
        }

        public Id() {
        }

        public Id(ClaseVehiculo claseVehiculo, Actividad actividad) {
            this.claseVehiculo = claseVehiculo;
            this.actividad = actividad;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;

            Id id = (Id) o;

            if (!actividad.equals(id.actividad)) return false;
            if (!claseVehiculo.equals(id.claseVehiculo)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = claseVehiculo.hashCode();
            result = 31 * result + actividad.hashCode();
            return result;
        }
    }
}
