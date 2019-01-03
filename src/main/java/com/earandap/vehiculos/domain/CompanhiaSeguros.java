package com.earandap.vehiculos.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Autor felix.batista
 */

@Entity
@Table(name = "companhia_seguros")
@DiscriminatorValue("COMPANHIA-SEGUROS")
public class CompanhiaSeguros extends Tercero {

    @Column(name = "companhia_nombre")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getNombreTercero() {
        return this.getNombre();
    }
}
