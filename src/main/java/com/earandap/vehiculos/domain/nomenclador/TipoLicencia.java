package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Angel Luis on 11/22/2015.
 */
@Entity
@DiscriminatorValue("TIPOLICENCIA")
public class TipoLicencia extends Nomenclador{
}
