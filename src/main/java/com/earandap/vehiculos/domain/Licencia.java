package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoLicencia;
import com.earandap.vehiculos.domain.nomenclador.TipoServicio;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Created by Angel Luis on 11/22/2015.
 */
@Entity
@Table(name = "licencia")
@SequenceGenerator(sequenceName = "licencia_seq", name = "SEQ_LICENCIA")
public class Licencia implements Serializable {

    @Id
    @Column(name = "licencia_id")
    @GeneratedValue(generator = "SEQ_LICENCIA", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tipolicencia_id")
    private TipoLicencia tipoLicencia;
    
    @ManyToOne
    @JoinColumn(name = "tiposervicio_id")
    private TipoServicio tipoServicio;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;

    @Column(name = "licencia_autorizado")
    private boolean autorizadoEmpresa;
    
    @Column(name = "licencia_vigencia")
    @Temporal(TemporalType.DATE)
    private Date vigencia;
    
    /*private String tipoLicenciaDescripcion;

    public String getTipoLicenciaDescripcion() {
        return this.tipoLicenciaDescripcion;
    }

    public void setTipoLicenciaDescripcion(String tipoLicenciaDescripcion) {
        this.tipoLicenciaDescripcion = tipoLicenciaDescripcion;
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TipoLicencia getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(TipoLicencia tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    public boolean isAutorizadoEmpresa() {
        return autorizadoEmpresa;
    }

    public void setAutorizadoEmpresa(boolean autorizadoEmpresa) {
        this.autorizadoEmpresa = autorizadoEmpresa;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }
}
