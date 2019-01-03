package com.earandap.vehiculos.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Angel Luis on 11/30/2015.
 */
@Entity
@Table(name = "investigacion")
@SequenceGenerator(sequenceName = "investigacion_seq", name = "SEQ_INVESTIGACION")
public class Investigacion implements Serializable {

    @Id
    @Column(name = "investigacion_id")
    @GeneratedValue(generator = "SEQ_INVESTIGACION", strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name="tercero_id")
    private Tercero tercero;

    @Column(name = "investigacion_relato")
    private String relato;
    
    @Column(name = "investigacion_leccion")
    private String leccion;
    
    @Column(name = "investigacion_socializada")
    private boolean socializada;
    
    @Column(name = "investigacion_mediodivulgacion")
    private String medioDivulgacion;

    @ManyToOne
    @JoinColumn(name = "incidente_id")
    private Incidente incidente;

    @Column(name = "investigacionn_fechainvestigacion")
    @Temporal(TemporalType.DATE)
    private Date fechaInvestigacion;

    public Date getFechaInvestigacion() {
        return fechaInvestigacion;
    }

    public void setFechaInvestigacion(Date fechaInvestigacion) {
        this.fechaInvestigacion = fechaInvestigacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Tercero getTercero() {
        return tercero;
    }

    public void setTercero(Tercero tercero) {
        this.tercero = tercero;
    }

    public String getRelato() {
        return relato;
    }

    public void setRelato(String relato) {
        this.relato = relato;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public String getLeccion() {
        return leccion;
    }

    public void setLeccion(String leccion) {
        this.leccion = leccion;
    }

    public boolean isSocializada() {
        return socializada;
    }

    public void setSocializada(boolean socializada) {
        this.socializada = socializada;
    }

    public String getMedioDivulgacion() {
        return medioDivulgacion;
    }

    public void setMedioDivulgacion(String medioDivulgacion) {
        this.medioDivulgacion = medioDivulgacion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Investigacion other = (Investigacion) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
