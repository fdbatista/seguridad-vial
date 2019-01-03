package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TIPOCOMBUSTIBLE")
public class TipoCombustible extends Nomenclador{
}
