package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Angel Luis on 11/27/2015.
 */
@Entity
@DiscriminatorValue("TIPOCONTRATO")
public class TipoContrato extends Nomenclador {
}
