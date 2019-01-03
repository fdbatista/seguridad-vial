package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.Resultado;

import javax.persistence.*;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "alistamiento_detalle")
@SequenceGenerator(sequenceName = "alistamiento_seq", name = "SEQ_ALISTAMIENTODETALLE")
public class AlistamientoDetalle {

    @Id
    @Column(name = "alistamientodetalle_id")
    @GeneratedValue(generator = "SEQ_ALISTAMIENTODETALLE", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "accionalistamiento_id")
    private AccionAlistamiento alistamiento;

    @ManyToOne
    @JoinColumn(name = "resultado_id")
    private Resultado resultado;

    @ManyToOne
    @JoinColumn(name = "actividad")
    private Actividad actividad;

    @Column(name = "alistamientodetalle_detalle")
    private String detalle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccionAlistamiento getAlistamiento() {
        return alistamiento;
    }

    public void setAlistamiento(AccionAlistamiento alistamiento) {
        this.alistamiento = alistamiento;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
