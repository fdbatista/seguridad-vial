package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TIPOINCIDENTE")
public class TipoIncidente extends Nomenclador{
}
