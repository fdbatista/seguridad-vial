package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Angel Luis on 10/23/2015.
 */
@Entity
@DiscriminatorValue("TIPOACTIVIDAD")
public class TipoActividad extends Nomenclador {
}
