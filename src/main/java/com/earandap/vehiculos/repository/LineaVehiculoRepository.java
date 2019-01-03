package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.nomenclador.LineaVehiculo;
import com.earandap.vehiculos.domain.nomenclador.MarcaVehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author felix.batista
 */
@Transactional
public interface LineaVehiculoRepository extends JpaRepository<LineaVehiculo, Long> {
    @Query("SELECT p FROM LineaVehiculo p WHERE p.marcaVehiculo = :marca")
    List<LineaVehiculo> getByIdMarca(@Param("marca") MarcaVehiculo marca);
}
