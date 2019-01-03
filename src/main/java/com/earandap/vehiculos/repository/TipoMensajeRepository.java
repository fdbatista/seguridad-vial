package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.TipoMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Angel Luis on 12/26/2015.
 */
@Transactional
public interface TipoMensajeRepository extends JpaRepository<TipoMensaje, Long> {

    Optional<Object> findOneByCodigo(String codigo);

    Optional<Object> findOneByDescripcion(String descripcion);

    List<TipoMensaje> findAllByIdNot(long id);
}
