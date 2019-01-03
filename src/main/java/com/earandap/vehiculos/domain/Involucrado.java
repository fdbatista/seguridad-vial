package com.earandap.vehiculos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
public class Involucrado {

    @EmbeddedId
    private Id id = new Id();

    @Embeddable
    public static class Id implements Serializable {

        @ManyToOne(cascade = CascadeType.REMOVE)
        private Persona persona;

        @ManyToOne(cascade = CascadeType.REMOVE)
        private Incidente incidente;

        public Persona getPersona() {
            return persona;
        }

        public void setPersona(Persona persona) {
            this.persona = persona;
        }

        public Incidente getIncidente() {
            return incidente;
        }

        public void setIncidente(Incidente incidente) {
            this.incidente = incidente;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(persona, id.persona) &&
                    Objects.equals(incidente, id.incidente);
        }

        @Override
        public int hashCode() {
            return Objects.hash(persona, incidente);
        }
    }
}
