package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.Nomenclador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@NoRepositoryBean
public interface NomencladorBaseRepository<T extends Nomenclador> extends CrudRepository<T, Long> {


}
