package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Angel Luis on 10/20/2015.
 */
@Entity
@Table(name = "persona_capacitacion")
@AssociationOverrides({
    @AssociationOverride(name = "id.persona",
            joinColumns = @JoinColumn(name = "persona_id"))
    ,
        @AssociationOverride(name = "id.capacitacion",
            joinColumns = @JoinColumn(name = "capacitacion_id"))})
public class PersonaCapacitacion implements Serializable {

    @EmbeddedId
    private Id id = new Id();

    @Transient
    private Persona persona;

    @Transient
    private Capacitacion capacitacion;

    public void setPersona(Persona persona) {
        id.setPersona(persona);
        this.persona = persona;
    }

    public Persona getPersona() {
        return id.getPersona();
    }

    @Column(name = "personacapacitacion_observaciones")
    private String observacion;

    @Column(name = "capacitacion_puntajeobtenido")
    private int puntajeObtenido;

    public int getPuntajeObtenido() {
        return puntajeObtenido;
    }

    public void setPuntajeObtenido(int puntajeObtenido) {
        this.puntajeObtenido = puntajeObtenido;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        id.setCapacitacion(capacitacion);
        this.capacitacion = capacitacion;
    }

    public Capacitacion getCapacitacion() {
        return id.getCapacitacion();
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
        this.persona = id.getPersona();
        this.capacitacion = id.getCapacitacion();
    }

    @Embeddable
    public static class Id implements Serializable {

        @ManyToOne
        private Persona persona;

        @ManyToOne
        private Capacitacion capacitacion;

        public Persona getPersona() {
            return persona;
        }

        public void setPersona(Persona persona) {
            this.persona = persona;
        }

        public Capacitacion getCapacitacion() {
            return capacitacion;
        }

        public void setCapacitacion(Capacitacion capacitacion) {
            this.capacitacion = capacitacion;
        }

        public Id() {
        }

        /*@Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Id)) {
                return false;
            }

            Id id = (Id) o;

            if (!capacitacion.equals(id.capacitacion)) {
                return false;
            }
            if (!persona.equals(id.persona)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = persona.hashCode();
            result = 31 * result + capacitacion.hashCode();
            return result;
        }*/

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.persona.getId());
            hash = 97 * hash + Objects.hashCode(this.capacitacion.getId());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Id other = (Id) obj;
            if (!Objects.equals(this.persona.getId(), other.persona.getId())) {
                return false;
            }
            if (!Objects.equals(this.capacitacion.getId(), other.capacitacion.getId())) {
                return false;
            }
            return true;
        }
        
    }
}
