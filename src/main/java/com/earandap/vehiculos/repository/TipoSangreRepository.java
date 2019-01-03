package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.TipoSangre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 10/8/2015.
 */
@Transactional
public interface TipoSangreRepository extends JpaRepository<TipoSangre, Long> {
}
