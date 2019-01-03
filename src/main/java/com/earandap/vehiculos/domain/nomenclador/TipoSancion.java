package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Entity
@DiscriminatorValue("TIPOSANCION")
public class TipoSancion extends Nomenclador {
}
