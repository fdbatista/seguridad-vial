package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.Cascade;

/**
 * Created by Angel Luis on 12/18/2015.
 */
@Entity
@Table(name = "notificacion")
@SequenceGenerator(sequenceName = "notifiacion_seq", name = "SEQ_NOTIFICACION")
public class Notificacion {

    @Id
    @Column(name = "notificacion_id")
    @GeneratedValue(generator = "SEQ_NOTIFICACION", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "notificacion_fechanotificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaNotificacion;

    @Column(name = "notificacion_encabezado")
    private String encabezado;

    @Column(name = "notificacion_detalle")
    private String detalle;

    @ManyToOne
    @JoinColumn(name = "tiponotificacion_id")
    private TipoNotificacion tipoNotificacion;

    @Column(name = "notificacion_fechacreacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "notificacion_procesada")
    private boolean procesada;

    @OneToMany(mappedBy = "id.notificacion", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<TerceroNotificacion> terceroNotificaciones = new LinkedHashSet<>();

    public Set<TerceroNotificacion> getTerceroNotificaciones() {
        return terceroNotificaciones;
    }

    public void setTerceroNotificaciones(Set<TerceroNotificacion> terceroNotificaciones) {
        this.terceroNotificaciones = terceroNotificaciones;
    }

    public boolean isProcesada() {
        return procesada;
    }

    public void setProcesada(boolean procesada) {
        this.procesada = procesada;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notificacion)) {
            return false;
        }

        Notificacion that = (Notificacion) o;

        if (id != that.id) {
            return false;
        }
        if (fechaNotificacion != null ? !fechaNotificacion.equals(that.fechaNotificacion) : that.fechaNotificacion != null) {
            return false;
        }
        if (encabezado != null ? !encabezado.equals(that.encabezado) : that.encabezado != null) {
            return false;
        }
        if (detalle != null ? !detalle.equals(that.detalle) : that.detalle != null) {
            return false;
        }
        if (tipoNotificacion != null ? !tipoNotificacion.equals(that.tipoNotificacion) : that.tipoNotificacion != null) {
            return false;
        }
        return !(fechaCreacion != null ? !fechaCreacion.equals(that.fechaCreacion) : that.fechaCreacion != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fechaNotificacion != null ? fechaNotificacion.hashCode() : 0);
        result = 31 * result + (encabezado != null ? encabezado.hashCode() : 0);
        result = 31 * result + (detalle != null ? detalle.hashCode() : 0);
        result = 31 * result + (tipoNotificacion != null ? tipoNotificacion.hashCode() : 0);
        result = 31 * result + (fechaCreacion != null ? fechaCreacion.hashCode() : 0);
        return result;
    }
}
