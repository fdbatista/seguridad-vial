package com.earandap.vehiculos.domain;

import javax.persistence.*;

/**
 * Created by Angel Luis on 12/1/2015.
 */
@Entity
@Table(name = "accidenteinvolucrado")
@SequenceGenerator(sequenceName = "accidenteinvolucrado_seq", name = "SEQ_ACCIDENTEINCOLUCRADO")
public class AccidenteInvolucrado {

    @Id
    @Column(name = "accidenteinvolucrado_id")
    @GeneratedValue(generator = "SEQ_ACCIDENTEINCOLUCRADO", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "incidente_id")
    private Incidente incidente;

    @ManyToOne
    @JoinColumn(name = "involucrado_id")
    private Persona involucrado;

    @Column(name = "accidenteinvolucrado_personalexterno")
    private boolean responsable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public Persona getInvolucrado() {
        return involucrado;
    }

    public void setInvolucrado(Persona involucrado) {
        this.involucrado = involucrado;
    }

    public boolean isResponsable() {
        return responsable;
    }

    public void setResponsable(boolean responsable) {
        this.responsable = responsable;
    }
}
