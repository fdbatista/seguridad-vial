package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoSeguro;
import java.io.Serializable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Angel Luis on 10/17/2015.
 */
@Entity
@Table(name = "seguro")
@SequenceGenerator(sequenceName = "seguro_seq", name = "SEQ_SEGURO")
public class Seguro implements Serializable {

    @Id
    @Column(name = "seguro_id")
    @GeneratedValue(generator = "SEQ_SEGURO", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "seguro_numero")
    private String numero;

    @Column(name = "seguro_fechaemision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;

    @Column(name = "seguro_fechavencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;

    @ManyToOne
    @JoinColumn(name = "tiposeguro_id")
    private TipoSeguro tipoSeguro;
    
    @ManyToOne
    @JoinColumn(name = "companhia_id")
    private CompanhiaSeguros companhia;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public TipoSeguro getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    
    public CompanhiaSeguros getCompanhia() {
        return companhia;
    }

    public void setCompanhia(CompanhiaSeguros companhia) {
        this.companhia = companhia;
    }
}
