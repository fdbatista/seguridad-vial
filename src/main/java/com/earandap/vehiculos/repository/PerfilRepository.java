package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Angel Luis on 11/9/2015.
 */
@Transactional
public interface PerfilRepository extends JpaRepository<Perfil,Long> {

    Optional<Object> findOneByNombre(String nombre);

    List<Perfil> findAllByIdNot(long id);
}
