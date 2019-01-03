package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TIPOCARROCERIA")
public class TipoCarroceria extends Nomenclador{
}
