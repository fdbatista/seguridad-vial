package com.earandap.vehiculos.domain;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;
import java.util.Date;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "accion")
@SequenceGenerator(sequenceName = "accion_seq", name = "SEQ_ACCION")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "accion_tipo", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
public abstract class Accion {

    @Id
    @Column(name = "accion_id")
    @GeneratedValue(generator = "SEQ_ACCION", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    protected Vehiculo vehiculo;

    @Column(name = "accion_fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "tercero_id")
    private Tercero tecnico;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Tercero getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tercero tecnico) {
        this.tecnico = tecnico;
    }


}
