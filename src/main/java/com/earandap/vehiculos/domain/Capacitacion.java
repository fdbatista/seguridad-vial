package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoActividad;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.Cascade;

/**
 * Created by Angel Luis on 10/19/2015.
 */
@Entity
@Table(name = "capacitacion")
@SequenceGenerator(sequenceName = "capacitacion_seq", name = "SEQ_CAPACITACION")
public class Capacitacion {

    @Id
    @Column(name = "capacitacion_id")
    @GeneratedValue(generator = "SEQ_CAPACITACION", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tipo_capacitacion")
    private TipoActividad tipoCapacitacion;
    
    @OneToMany(mappedBy = "capacitacion", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<CapacitacionDocumento> documentosCapacitacion = new LinkedHashSet<>();

    @Column(name = "capacitacion_fechacapacitacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCapacitacion;

    @Column(name = "capacitacion_fechavencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    
    @Column(name = "capacitacion_fechaprogramacion")
    @Temporal(TemporalType.DATE)
    private Date fechaProgramacion;
    
    @Column(name = "capacitacion_realizada")
    private boolean capacRealizada;
    
    @Column(name = "capacitacion_puntajemaximo")
    private int puntajeMaximo;

    @Column(name = "capacitacion_observaciones")
    private String observaciones;

    @Column(name = "capacitacion_nombre")
    private String nombreCapacitacion;

    @OneToMany(mappedBy = "id.capacitacion", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Set<PersonaCapacitacion> personas = new LinkedHashSet<>();

    public Set<CapacitacionDocumento> getDocumentosCapacitacion() {
        return documentosCapacitacion;
    }

    public void setDocumentosCapacitacion(Set<CapacitacionDocumento> documentosCapacitacion) {
        this.documentosCapacitacion = documentosCapacitacion;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public Date getFechaProgramacion() {
        return fechaProgramacion;
    }

    public boolean isCapacRealizada() {
        return capacRealizada;
    }

    public void setCapacRealizada(boolean capacRealizada) {
        this.capacRealizada = capacRealizada;
    }

    public void setFechaProgramacion(Date fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TipoActividad getTipoCapacitacion() {
        return tipoCapacitacion;
    }

    public void setTipoCapacitacion(TipoActividad tipoCapacitacion) {
        this.tipoCapacitacion = tipoCapacitacion;
    }

    public Date getFechaCapacitacion() {
        return fechaCapacitacion;
    }

    public void setFechaCapacitacion(Date fechaCapacitacion) {
        this.fechaCapacitacion = fechaCapacitacion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<PersonaCapacitacion> getPersonas() {
        return personas;
    }

    public void setPersonas(Set<PersonaCapacitacion> personas) {
        this.personas = personas;
    }

    public String getNombreCapacitacion() {
        return nombreCapacitacion;
    }

    public void setNombreCapacitacion(String nombreCapacitacion) {
        this.nombreCapacitacion = nombreCapacitacion;
    }
}
