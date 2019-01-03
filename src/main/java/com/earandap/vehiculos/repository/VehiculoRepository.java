package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Transactional
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findOneByPlaca(String placa);

    @Query("SELECT v FROM Vehiculo v WHERE LOWER(v.placa) LIKE LOWER(CONCAT ('%',:query, '%'))")
    List<Vehiculo> search(@Param("query") String query);
}
