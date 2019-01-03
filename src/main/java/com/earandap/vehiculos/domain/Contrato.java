package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoContrato;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Angel Luis on 11/27/2015.
 */
@Entity
@Table(name = "contrato")
@SequenceGenerator(sequenceName = "contrato_seq", name = "SEQ_CONTRATO")
public class Contrato {

    @Id
    @Column(name = "contrato_id")
    @GeneratedValue(generator = "SEQ_CONTRATO", strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name="tercero_id")
    private Tercero tercero;

    @ManyToOne
    @JoinColumn(name = "tipocontrato_id")
    private TipoContrato tipoContrato;

    @Column(name = "contrato_numero")
    private int numeroContrato;

    @Column(name = "contrato_duracion")
    private int duracionContrato;

    @Column(name = "contrato_fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "contrato_inactivo")
    private boolean inactivo;

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

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public int getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(int numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public int getDuracionContrato() {
        return duracionContrato;
    }

    public void setDuracionContrato(int duracionContrato) {
        this.duracionContrato = duracionContrato;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public boolean isInactivo() {
        return inactivo;
    }

    public void setInactivo(boolean inactivo) {
        this.inactivo = inactivo;
    }
}
