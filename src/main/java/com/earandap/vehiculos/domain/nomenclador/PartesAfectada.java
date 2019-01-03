package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@DiscriminatorValue("PARTEAFECTADA")
public class PartesAfectada extends Nomenclador {

    @Override
    public String toString() {
        return this.getDescripcion();
    }
    
}
