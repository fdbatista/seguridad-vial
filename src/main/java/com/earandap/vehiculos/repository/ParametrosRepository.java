package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Parametro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Transactional
public interface ParametrosRepository extends JpaRepository<Parametro, Long> {
    
    @Query("SELECT p FROM Parametro p WHERE p.id > 3 order by p.id")
    List<Parametro> findParametrosCorreo();
    
}
