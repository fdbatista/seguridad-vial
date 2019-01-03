package com.earandap.vehiculos.domain.nomenclador;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@DiscriminatorValue("MUNICIPIO")
public class Municipio extends Nomenclador {

}
