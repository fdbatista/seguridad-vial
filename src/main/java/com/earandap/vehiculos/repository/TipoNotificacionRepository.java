package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Angel Luis on 12/24/2015.
 */
@Transactional
public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Long>{

    Optional<Object> findOneByCodigo(String codigo);

    Optional<Object> findOneByDescripcion(String descripcion);

    List<TipoNotificacion> findAllByIdNot(long id);
}
