package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.Actividad;
import com.earandap.vehiculos.domain.nomenclador.Resultado;

import javax.persistence.*;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "mantenimiento_detalle")
@SequenceGenerator(sequenceName = "mantenimientodetalle_seq", name = "SEQ_MANTENIMIENTODETALLE")
public class MantenimientoDetalle {

    @Id
    @Column(name = "mantenimientodetalle_id")
    @GeneratedValue(generator = "SEQ_MANTENIMIENTODETALLE", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "accionmantenimiento_id")
    private AccionMantenimiento mantenimiento;

    @ManyToOne
    @JoinColumn(name = "resultado_id")
    private Resultado resultado;

    @ManyToOne
    @JoinColumn(name = "actividad")
    private Actividad actividad;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccionMantenimiento getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(AccionMantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
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
}
