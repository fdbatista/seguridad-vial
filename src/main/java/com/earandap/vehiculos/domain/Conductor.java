package com.earandap.vehiculos.domain;

import java.io.Serializable;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "conductor")
@SequenceGenerator(sequenceName = "conductor_seq", name = "SEQ_CONDUCTOR")
public class Conductor implements Serializable {

    @Id
    @Column(name = "conductor_id")
    @GeneratedValue(generator = "SEQ_CONDUCTOR", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "conductor_annolicencia")
    @Temporal(TemporalType.DATE)
    private Date annoLicencia;
    
    @Column(name = "conductor_numlicencia", length = 30)
    private String numeroLicencia;
    
    @Column(name = "conductor_restricciones", length = 100)
    private String restricciones;
    
    @Column(name = "conductor_organismoexp", length = 100)
    private String organismoExpedidor;

    @OneToMany(mappedBy = "conductor", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Licencia> licencias = new LinkedHashSet<>();

    @OneToOne(mappedBy = "conductor")
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Persona persona;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getAnnoLicencia() {
        return annoLicencia;
    }

    public void setAnnoLicencia(Date annoLicencia) {
        this.annoLicencia = annoLicencia;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Set<Licencia> getLicencias() {
        return licencias;
    }

    public void setLicencias(Set<Licencia> licencias) {
        this.licencias = licencias;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(String restricciones) {
        this.restricciones = restricciones;
    }

    public String getOrganismoExpedidor() {
        return organismoExpedidor;
    }

    public void setOrganismoExpedidor(String organismoExpedidor) {
        this.organismoExpedidor = organismoExpedidor;
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
        final Conductor other = (Conductor) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
