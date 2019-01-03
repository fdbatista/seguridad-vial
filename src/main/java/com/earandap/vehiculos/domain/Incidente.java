package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.*;
import java.io.Serializable;
import java.util.ArrayList;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "incidente")
@SequenceGenerator(sequenceName = "incidente_seq", name = "SEQ_INCIDENTE")
public class Incidente implements Serializable {

    @Id
    @Column(name = "incidente_id")
    @GeneratedValue(generator = "SEQ_INCIDENTE", strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "incidente_fecha")
    private Date fecha;

    @Column(name = "incidente_observaciones")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @Column(name = "incidente_direccion")
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zona;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Persona responsable;

    @ManyToOne
    @JoinColumn(name = "reportero_id")
    private Persona reportero;

    @ManyToOne
    @JoinColumn(name = "tipojornada_id")
    private TipoJornada tipoJornada;

    @Column(name = "incidente_tiempoconduccion")
    private int tiempoConduccion;

    @Column(name = "incidente_accidentetrabajo")
    private boolean accidenteTrabajo;

    @Column(name = "incidente_accidentetransito")
    private boolean accidenteTransito;

    @Column(name = "incidente_incapacidad")
    private boolean incapacidad;

    @Column(name = "incidente_diasincapacidad")
    private int diasIncapacidad;
    
    @Column(name = "incidente_personalexterno")
    private boolean personalExterno;
    
    @Column(name = "incidente_factorespersonales", length = 150)
    private String factoresPersonales;
    
    @Column(name = "incidente_factorestrabajo", length = 150)
    private String factoresTrabajo;
    
    @Column(name = "incidente_lesionadosempresa", nullable = true)
    private int lesionadosEmpresa;
    
    @Column(name = "incidente_condlesionadosempresa", nullable = true)
    private int condLesionadosEmpresa;
    
    @Column(name = "incidente_mortalidadsempresa", nullable = true)
    private int mortalidadEmpresa;
    
    @Column(name = "incidente_mortalidadterceros", nullable = true)
    private int mortalidadTerceros;
    
    @Column(name = "incidente_indemnizacion", nullable = true)
    private float indemnizacion;

    @ManyToOne
    @JoinColumn(name = "tipoevento_id")
    private TipoEvento tipoEvento;

    @OneToMany(mappedBy = "incidente", fetch = FetchType.EAGER)
    private Set<Archivo> archivos = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "incidente_parteafectada", joinColumns = {
            @JoinColumn(name = "incidente_id")}, inverseJoinColumns = {
            @JoinColumn(name = "parteafectada_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<PartesAfectada> partesAfectadas = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "incidente_causainmediata", joinColumns = {
            @JoinColumn(name = "incidente_id")}, inverseJoinColumns = {
            @JoinColumn(name = "causainmediata_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<CausaInmediata> causaInmediatas = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "incidente_causabasica", joinColumns = {
            @JoinColumn(name = "incidente_id")}, inverseJoinColumns = {
            @JoinColumn(name = "causabasica_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<CausaBasica> causaBasicas = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "incidente_perdida", joinColumns = {
            @JoinColumn(name = "incidente_id")}, inverseJoinColumns = {
            @JoinColumn(name = "perdida_id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Perdida> perdidas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "incidente", fetch = FetchType.EAGER)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private Set<Infraccion> infracciones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "incidente", fetch = FetchType.EAGER)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private Set<Investigacion> investigaciones = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "incidente", fetch = FetchType.EAGER)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private Set<AccidenteInvolucrado> accidenteInvolucrados = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "incidente", fetch = FetchType.EAGER)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private Set<IncidenteLesion> lesiones = new LinkedHashSet<>();

    public List<IncidenteLesion> getListaLesiones() {
        List<IncidenteLesion> res = new ArrayList<IncidenteLesion>();
        if (lesiones != null)
        {
                for (IncidenteLesion incidenteLesion : lesiones) {
                res.add(incidenteLesion);
            }
        }
        return res;
    }

    public int getCondLesionadosEmpresa() {
        return condLesionadosEmpresa;
    }

    public void setCondLesionadosEmpresa(int condLesionadosEmpresa) {
        this.condLesionadosEmpresa = condLesionadosEmpresa;
    }

    public int getLesionadosEmpresa() {
        return lesionadosEmpresa;
    }

    public void setLesionadosEmpresa(int lesionadosEmpresa) {
        this.lesionadosEmpresa = lesionadosEmpresa;
    }

    public int getMortalidadEmpresa() {
        return mortalidadEmpresa;
    }

    public void setMortalidadEmpresa(int mortalidadEmpresa) {
        this.mortalidadEmpresa = mortalidadEmpresa;
    }

    public int getMortalidadTerceros() {
        return mortalidadTerceros;
    }

    public void setMortalidadTerceros(int mortalidadTerceros) {
        this.mortalidadTerceros = mortalidadTerceros;
    }

    public float getIndemnizacion() {
        return indemnizacion;
    }

    public void setIndemnizacion(float indemnizacion) {
        this.indemnizacion = indemnizacion;
    }
    
    public Set<IncidenteLesion> getLesiones() {
        return lesiones;
    }

    public void setLesiones(Set<IncidenteLesion> lesiones) {
        this.lesiones = lesiones;
    }
    
    public String getFactoresPersonales() {
        return factoresPersonales;
    }

    public void setFactoresPersonales(String factoresPersonales) {
        this.factoresPersonales = factoresPersonales;
    }

    public String getFactoresTrabajo() {
        return factoresTrabajo;
    }

    public void setFactoresTrabajo(String factoresTrabajo) {
        this.factoresTrabajo = factoresTrabajo;
    }
    
    public boolean isPersonalExterno() {
        return personalExterno;
    }

    public void setPersonalExterno(boolean personalExterno) {
        this.personalExterno = personalExterno;
    }

    public Set<AccidenteInvolucrado> getAccidenteInvolucrados() {
        return accidenteInvolucrados;
    }

    public void setAccidenteInvolucrados(Set<AccidenteInvolucrado> accidenteInvolucrados) {
        this.accidenteInvolucrados = accidenteInvolucrados;
    }

    public Set<Investigacion> getInvestigaciones() {
        return investigaciones;
    }

    public void setInvestigaciones(Set<Investigacion> investigaciones) {
        this.investigaciones = investigaciones;
    }

    public Set<Infraccion> getInfracciones() {
        return infracciones;
    }

    public void setInfracciones(Set<Infraccion> infracciones) {
        this.infracciones = infracciones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Persona getResponsable() {
        return responsable;
    }

    public void setResponsable(Persona responsable) {
        this.responsable = responsable;
    }

    public Persona getReportero() {
        return reportero;
    }

    public void setReportero(Persona reportero) {
        this.reportero = reportero;
    }

    public TipoJornada getTipoJornada() {
        return tipoJornada;
    }

    public void setTipoJornada(TipoJornada tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    public int getTiempoConduccion() {
        return tiempoConduccion;
    }

    public void setTiempoConduccion(int tiempoConduccion) {
        this.tiempoConduccion = tiempoConduccion;
    }

    public boolean isAccidenteTrabajo() {
        return accidenteTrabajo;
    }

    public void setAccidenteTrabajo(boolean accidenteTrabajo) {
        this.accidenteTrabajo = accidenteTrabajo;
    }

    public boolean isIncapacidad() {
        return incapacidad;
    }

    public void setIncapacidad(boolean incapacidad) {
        this.incapacidad = incapacidad;
    }

    public int getDiasIncapacidad() {
        return diasIncapacidad;
    }

    public void setDiasIncapacidad(int diasIncapacidad) {
        this.diasIncapacidad = diasIncapacidad;
    }

    public Set<Archivo> getArchivos() {
        return archivos;
    }

    public void setArchivos(Set<Archivo> archivos) {
        this.archivos = archivos;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public boolean isAccidenteTransito() {
        return accidenteTransito;
    }

    public void setAccidenteTransito(boolean accidenteTransito) {
        this.accidenteTransito = accidenteTransito;
    }

    public Set<PartesAfectada> getPartesAfectadas() {
        return partesAfectadas;
    }

    public void setPartesAfectadas(Set<PartesAfectada> partesAfectadas) {
        this.partesAfectadas = partesAfectadas;
    }

    public Set<CausaInmediata> getCausaInmediatas() {
        return causaInmediatas;
    }

    public void setCausaInmediatas(Set<CausaInmediata> causaInmediatas) {
        this.causaInmediatas = causaInmediatas;
    }

    public Set<CausaBasica> getCausaBasicas() {
        return causaBasicas;
    }

    public void setCausaBasicas(Set<CausaBasica> causaBasicas) {
        this.causaBasicas = causaBasicas;
    }

    public Set<Perdida> getPerdidas() {
        return perdidas;
    }

    public void setPerdidas(Set<Perdida> perdidas) {
        this.perdidas = perdidas;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Incidente)) return false;

        Incidente incidente = (Incidente) o;

        if (id != incidente.id) return false;
        if (tiempoConduccion != incidente.tiempoConduccion) return false;
        if (accidenteTrabajo != incidente.accidenteTrabajo) return false;
        if (accidenteTransito != incidente.accidenteTransito) return false;
        if (incapacidad != incidente.incapacidad) return false;
        if (diasIncapacidad != incidente.diasIncapacidad) return false;
        if (fecha != null ? !fecha.equals(incidente.fecha) : incidente.fecha != null) return false;
        if (observaciones != null ? !observaciones.equals(incidente.observaciones) : incidente.observaciones != null)
            return false;
        if (conductor != null ? !conductor.equals(incidente.conductor) : incidente.conductor != null) return false;
        if (vehiculo != null ? !vehiculo.equals(incidente.vehiculo) : incidente.vehiculo != null) return false;
        if (direccion != null ? !direccion.equals(incidente.direccion) : incidente.direccion != null) return false;
        if (zona != null ? !zona.equals(incidente.zona) : incidente.zona != null) return false;
        if (responsable != null ? !responsable.equals(incidente.responsable) : incidente.responsable != null)
            return false;
        if (reportero != null ? !reportero.equals(incidente.reportero) : incidente.reportero != null) return false;
        if (tipoJornada != null ? !tipoJornada.equals(incidente.tipoJornada) : incidente.tipoJornada != null)
            return false;
        if (tipoEvento != null ? !tipoEvento.equals(incidente.tipoEvento) : incidente.tipoEvento != null) return false;
        if (archivos != null ? !archivos.equals(incidente.archivos) : incidente.archivos != null) return false;
        if (partesAfectadas != null ? !partesAfectadas.equals(incidente.partesAfectadas) : incidente.partesAfectadas != null)
            return false;
        if (causaInmediatas != null ? !causaInmediatas.equals(incidente.causaInmediatas) : incidente.causaInmediatas != null)
            return false;
        if (causaBasicas != null ? !causaBasicas.equals(incidente.causaBasicas) : incidente.causaBasicas != null)
            return false;
        return !(perdidas != null ? !perdidas.equals(incidente.perdidas) : incidente.perdidas != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        result = 31 * result + (observaciones != null ? observaciones.hashCode() : 0);
        result = 31 * result + (conductor != null ? conductor.hashCode() : 0);
        result = 31 * result + (vehiculo != null ? vehiculo.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (zona != null ? zona.hashCode() : 0);
        result = 31 * result + (responsable != null ? responsable.hashCode() : 0);
        result = 31 * result + (reportero != null ? reportero.hashCode() : 0);
        result = 31 * result + (tipoJornada != null ? tipoJornada.hashCode() : 0);
        result = 31 * result + tiempoConduccion;
        result = 31 * result + (accidenteTrabajo ? 1 : 0);
        result = 31 * result + (accidenteTransito ? 1 : 0);
        result = 31 * result + (incapacidad ? 1 : 0);
        result = 31 * result + diasIncapacidad;
        result = 31 * result + (tipoEvento != null ? tipoEvento.hashCode() : 0);
        result = 31 * result + (archivos != null ? archivos.hashCode() : 0);
        result = 31 * result + (partesAfectadas != null ? partesAfectadas.hashCode() : 0);
        result = 31 * result + (causaInmediatas != null ? causaInmediatas.hashCode() : 0);
        result = 31 * result + (causaBasicas != null ? causaBasicas.hashCode() : 0);
        result = 31 * result + (perdidas != null ? perdidas.hashCode() : 0);
        return result;
    }*/

    public String getNombreConductorVehiculo(){
        return conductor.getPersona().getNombreCompleto() + " - " + vehiculo.getPlaca();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Incidente other = (Incidente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
