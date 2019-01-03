package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Angel Luis on 11/6/2015.
 */
@Entity
@DiscriminatorValue("TIPORECURSO")
public class TipoRecurso extends Nomenclador{
}
