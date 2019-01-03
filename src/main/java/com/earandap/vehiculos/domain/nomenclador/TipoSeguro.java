package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Angel Luis on 10/17/2015.
 */
@Entity
@DiscriminatorValue("TIPOSEGURO")
public class TipoSeguro extends Nomenclador{

    @Override
    public String toString() {
        return getDescripcion();
    }
}
