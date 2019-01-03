package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.MiembroComite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by felix.batista.
 */
@Transactional
public interface MiembroComiteRepository extends JpaRepository<MiembroComite, Long> {
}
