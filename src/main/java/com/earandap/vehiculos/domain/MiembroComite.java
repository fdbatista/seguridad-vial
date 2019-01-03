package com.earandap.vehiculos.domain;

import java.io.Serializable;

import javax.persistence.*;

/**
 * @Autor felix.batista
 */
@Entity
@Table(name = "miembro_comite")
@SequenceGenerator(sequenceName = "miembro_comite_seq", name = "SEQ_MIEMBRO_COMITE")
public class MiembroComite implements Serializable {

    @Id
    @Column(name = "miembro_comite_id")
    @GeneratedValue(generator = "SEQ_MIEMBRO_COMITE", strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "cargo")
    private String cargo;
    
    @Column(name = "es_responsable")
    private boolean esResponsable;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    @ManyToOne
    @JoinColumn(name = "miembro_id")
    private Persona miembro;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public boolean isEsResponsable() {
        return esResponsable;
    }

    public void setEsResponsable(boolean esResponsable) {
        this.esResponsable = esResponsable;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Persona getMiembro() {
        return miembro;
    }

    public void setMiembro(Persona miembro) {
        this.miembro = miembro;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final MiembroComite other = (MiembroComite) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
