package com.earandap.vehiculos.domain;

import java.io.Serializable;

import javax.persistence.*;

/**
 * @Autor felix.batista
 */
@Entity
@Table(name = "mantenimiento_documento")
@SequenceGenerator(sequenceName = "mantenimiento_documento_seq", name = "SEQ_MANTENIMIENTO_DOCUMENTO")
public class MantenimientoDocumento implements Serializable {

    @Id
    @Column(name = "mantenimiento_documento_id")
    @GeneratedValue(generator = "SEQ_MANTENIMIENTO_DOCUMENTO", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "ruta_documento")
    private String rutaDocumento;

    @ManyToOne
    @JoinColumn(name = "mantenimiento_id")
    private AccionMantenimiento mantenimiento;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccionMantenimiento getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(AccionMantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final MantenimientoDocumento other = (MantenimientoDocumento) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
