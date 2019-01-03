package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.ClaseVehiculo;
import com.earandap.vehiculos.domain.nomenclador.TipoMantenimiento;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "mantenimiento_actividad")
@AssociationOverrides({
    @AssociationOverride(name = "id.claseVehiculo",
            joinColumns = @JoinColumn(name = "clasevehiculo_id"))
    ,
        @AssociationOverride(name = "id.tipoMantenimiento",
            joinColumns = @JoinColumn(name = "tipomantenimiento_id"))
    ,
        @AssociationOverride(name = "id.actividad",
            joinColumns = @JoinColumn(name = "actividad_id"))})
public class MantenimientoActividad {

    @EmbeddedId
    private Id id = new Id();

    @Transient
    private ClaseVehiculo claseVehiculo;

    @Transient
    private Actividad actividad;

    @Transient
    private TipoMantenimiento tipoMantenimiento;

    @Column(name = "mantenimientoactividad_tiemporeposicion")
    private int tiempoReposicion;

    @Column(name = "mantenimientoactividad_kmreposicion")
    private int kmReposicion;

    @Column(name = "mantenimientoactividad_inactivo")
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

    public TipoMantenimiento getTipoMantenimiento() {
        return id.tipoMantenimiento;
    }

    public void setTipoMantenimiento(TipoMantenimiento tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
        id.setTipoMantenimiento(tipoMantenimiento);
    }

    public int getTiempoReposicion() {
        return tiempoReposicion;
    }

    public void setTiempoReposicion(int tiempoReposicion) {
        this.tiempoReposicion = tiempoReposicion;
    }

    public int getKmReposicion() {
        return kmReposicion;
    }

    public void setKmReposicion(int kmReposicion) {
        this.kmReposicion = kmReposicion;
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

        @ManyToOne
        private TipoMantenimiento tipoMantenimiento;

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

        public TipoMantenimiento getTipoMantenimiento() {
            return tipoMantenimiento;
        }

        public void setTipoMantenimiento(TipoMantenimiento tipoMantenimiento) {
            this.tipoMantenimiento = tipoMantenimiento;
        }

        public Id() {
        }

        public Id(ClaseVehiculo claseVehiculo, Actividad actividad, TipoMantenimiento tipoMantenimiento) {
            this.claseVehiculo = claseVehiculo;
            this.actividad = actividad;
            this.tipoMantenimiento = tipoMantenimiento;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Id)) {
                return false;
            }

            Id id = (Id) o;

            if (!actividad.equals(id.actividad)) {
                return false;
            }
            if (!claseVehiculo.equals(id.claseVehiculo)) {
                return false;
            }
            if (!tipoMantenimiento.equals(id.tipoMantenimiento)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = claseVehiculo.hashCode();
            result = 31 * result + actividad.hashCode();
            result = 31 * result + tipoMantenimiento.hashCode();
            return result;
        }
    }

}
