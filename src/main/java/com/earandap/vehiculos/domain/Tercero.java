package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */

@Entity
@Table(name = "tercero")
@SequenceGenerator(sequenceName = "tercero_seq", name = "SEQ_TERCERO")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tercero_tipo", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
public abstract class Tercero {

    @Id
    @Column(name = "tercero_id")
    @GeneratedValue(generator = "SEQ_TERCERO", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tipodocumento_id")
    private TipoDocumento tipoDocumento;

    @Column(name = "tercero_documento")
    private String documento;

    @Email
    @Column(name = "tercero_correo")
    private String correo;

    @Column(name = "tercero_telefono")
    private String telefono;

    @Column(name = "tercero_direccion")
    private String direccion;

    @Column(name = "tercero_observacion")
    private String observacion;

    @Column(name = "tercero_inactivo")
    private boolean inactivo;

    public boolean isInactivo() {
        return inactivo;
    }

    public void setInactivo(boolean inactivo) {
        this.inactivo = inactivo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tercero)) return false;

        Tercero tercero = (Tercero) o;

        if (id != tercero.id) return false;
        if (!documento.equals(tercero.documento)) return false;
        if (!tipoDocumento.equals(tercero.tipoDocumento)) return false;

        return true;
    }
    
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + tipoDocumento.hashCode();
        result = 31 * result + documento.hashCode();
        return result;
    }*/

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
        final Tercero other = (Tercero) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
    
    

    public abstract String getNombreTercero();

    @Override
    public String toString() {
        return getNombreTercero();
    }
}
