package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoDocumentoEmpresa;
import java.io.Serializable;

import javax.persistence.*;

/**
 * @Autor felix.batista
 */
@Entity
@Table(name = "empresa_documento")
@SequenceGenerator(sequenceName = "empresa_documento_seq", name = "SEQ_EMPRESA_DOCUMENTO")
public class EmpresaDocumento implements Serializable {

    @Id
    @Column(name = "empresa_documento_id")
    @GeneratedValue(generator = "SEQ_EMPRESA_DOCUMENTO", strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "ruta_documento")
    private String rutaDocumento;
    
    @ManyToOne
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumentoEmpresa tipoDocumentoEmpresa;
    
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public TipoDocumentoEmpresa getTipoDocumentoEmpresa() {
        return tipoDocumentoEmpresa;
    }

    public void setTipoDocumentoEmpresa(TipoDocumentoEmpresa tipoDocumentoEmpresa) {
        this.tipoDocumentoEmpresa = tipoDocumentoEmpresa;
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
        final EmpresaDocumento other = (EmpresaDocumento) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
