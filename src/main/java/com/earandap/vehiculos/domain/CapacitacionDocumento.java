package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoDocumentoCapacitacion;
import java.io.Serializable;

import javax.persistence.*;

/**
 * @Autor felix.batista
 */
@Entity
@Table(name = "capacitacion_documento")
@SequenceGenerator(sequenceName = "capacitacion_documento_seq", name = "SEQ_CAPACITACION_DOCUMENTO")
public class CapacitacionDocumento implements Serializable {

    @Id
    @Column(name = "capacitacion_documento_id")
    @GeneratedValue(generator = "SEQ_CAPACITACION_DOCUMENTO", strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "ruta_documento")
    private String rutaDocumento;
    
    @ManyToOne
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumentoCapacitacion tipoDocumentoCapacitacion;
    
    @ManyToOne
    @JoinColumn(name = "capacitacion_id")
    private Capacitacion capacitacion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Capacitacion getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(Capacitacion capacitacion) {
        this.capacitacion = capacitacion;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public TipoDocumentoCapacitacion getTipoDocumentoCapacitacion() {
        return tipoDocumentoCapacitacion;
    }

    public void setTipoDocumentoCapacitacion(TipoDocumentoCapacitacion tipoDocumentoCapacitacion) {
        this.tipoDocumentoCapacitacion = tipoDocumentoCapacitacion;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CapacitacionDocumento other = (CapacitacionDocumento) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
