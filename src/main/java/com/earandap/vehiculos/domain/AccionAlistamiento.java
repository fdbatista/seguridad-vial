package com.earandap.vehiculos.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "accion_alistamiento")
@DiscriminatorValue("ACCIONALISTAMIENTO")
public class AccionAlistamiento extends Accion implements Serializable {

    @Column(name = "accionalistamiento_operativo")
    private boolean operativo;

    @Column(name = "accionalistamiento_detalles")
    private String detalle;
    
    @Column(name = "accionalistamiento_kmactual")
    private int kmActual;

    @OneToMany(mappedBy = "alistamiento", fetch = FetchType.EAGER)
    private Set<AlistamientoDetalle> detalles = new LinkedHashSet<>();

    public int getKmActual() {
        return kmActual;
    }

    public void setKmActual(int kmActual) {
        this.kmActual = kmActual;
    }
    
    public boolean isOperativo() {
        return operativo;
    }

    public void setOperativo(boolean operativo) {
        this.operativo = operativo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public void setDetalles(Set<AlistamientoDetalle> detalles) {
        this.detalles = detalles;
    }

    public Set<AlistamientoDetalle> getDetalles() {
        return detalles;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = (int) (59 * hash + this.getId());
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
        final AccionAlistamiento other = (AccionAlistamiento) obj;
        return this.getId() == other.getId();
    }
}
