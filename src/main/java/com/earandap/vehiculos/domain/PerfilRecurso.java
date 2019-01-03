package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Angel Luis on 11/6/2015.
 */
@Entity
@Table(name = "perfil_recurso")
@AssociationOverrides({
        @AssociationOverride(name = "id.perfil",
                joinColumns = @JoinColumn(name = "perfil_id")),
        @AssociationOverride(name = "id.recurso",
                joinColumns = @JoinColumn(name = "recurso_id")) })
public class PerfilRecurso {

    @EmbeddedId
    private Id id = new Id();

    @Transient
    private Perfil perfil;

    @Transient
    private Recurso recurso;

    @Column(name = "perfilrecurso_consultar")
    private boolean consultar = false;

    @Column(name = "perfilrecurso_crear")
    private boolean crear = false;

    @Column(name = "perfilrecurso_modificar")
    private boolean modificar = false;

    @Column(name = "perfilrecurso_eliminar")
    private boolean eliminar = false;

    @ManyToOne
    @JoinColumn(name = "submodulo_id")
    private SubModulo subModulo;

    public boolean isEliminar() {
        return eliminar;
    }

    public SubModulo getSubModulo() {
        return subModulo;
    }

    public void setSubModulo(SubModulo subModulo) {
        this.subModulo = subModulo;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Perfil getPerfil() {
        return id.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        this.id.perfil = perfil;
    }

    public Recurso getRecurso() {
        return id.recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
        this.id.recurso = recurso;
    }

    public boolean isConsultar() {
        return consultar;
    }

    public void setConsultar(boolean consultar) {
        this.consultar = consultar;
    }

    public boolean isCrear() {
        return crear;
    }

    public void setCrear(boolean crear) {
        this.crear = crear;
    }

    public boolean isModificar() {
        return modificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public boolean getEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

    @Embeddable
    public static class Id implements Serializable {

        @ManyToOne
        private Perfil perfil;

        @ManyToOne
        private Recurso recurso;

        public Id() {
        }

        public Id(Perfil perfil, Recurso recurso) {
            this.perfil = perfil;
            this.recurso = recurso;
        }

        public Perfil getPerfil() {
            return perfil;
        }

        public void setPerfil(Perfil perfil) {
            this.perfil = perfil;
        }

        public Recurso getRecurso() {
            return recurso;
        }

        public void setRecurso(Recurso recurso) {
            this.recurso = recurso;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;

            Id id = (Id) o;

            if (!perfil.equals(id.perfil)) return false;
            if (!recurso.equals(id.recurso)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = perfil.hashCode();
            result = 31 * result + recurso.hashCode();
            return result;
        }
    }


}
