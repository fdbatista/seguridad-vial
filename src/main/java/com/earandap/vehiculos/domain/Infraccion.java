package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoIncidente;
import com.earandap.vehiculos.domain.nomenclador.TipoSancion;
import java.io.Serializable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Entity
@Table(name = "infraccion")
@SequenceGenerator(sequenceName = "infraccion_seq", name = "SEQ_INFRACCION")
public class Infraccion implements Serializable {

    @Id
    @Column(name = "infraccion_id")
    @GeneratedValue(generator = "SEQ_INFRACCION", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "infraccion_causa")
    private String causa;

    @Column(name = "infraccion_fechainfraccion")
    @Temporal(TemporalType.DATE)
    private Date fechaInfraccion;

    @Column(name = "infraccion_fechapago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;

    @Column(name = "infraccion_valormulta")
    private double valorMulta;

    @Column(name = "infraccion_detalle")
    private String detalle;

    @ManyToOne
    @JoinColumn(name = "tiposancion_id")
    private TipoSancion tipoSancion;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Persona conductor;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "incidente_id")
    private Incidente incidente;
    
    @ManyToOne
    @JoinColumn(name = "tipoincidente_id")
    private TipoIncidente tipoIncidente;

    public TipoIncidente getTipoIncidente() {
        return tipoIncidente;
    }

    public void setTipoIncidente(TipoIncidente tipoIncidente) {
        this.tipoIncidente = tipoIncidente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public Date getFechaInfraccion() {
        return fechaInfraccion;
    }

    public void setFechaInfraccion(Date fechaInfraccion) {
        this.fechaInfraccion = fechaInfraccion;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(double valorMulta) {
        this.valorMulta = valorMulta;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public TipoSancion getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(TipoSancion tipoSancion) {
        this.tipoSancion = tipoSancion;
    }

    public Persona getConductor() {
        return conductor;
    }

    public void setConductor(Persona conductor) {
        this.conductor = conductor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Infraccion other = (Infraccion) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
