package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Angel Luis on 10/3/2015.
 */
@Transactional
public interface EmpresaRepository extends JpaRepository<Empresa, Long>{

    @Query("SELECT e FROM Empresa e WHERE e.documento = :documento")
    Empresa searchEmpresa(@Param("documento") String documento);

    @Query("SELECT e FROM Empresa e WHERE LOWER(e.razonSocial) LIKE LOWER(CONCAT ('%',:query, '%'))")
    List<Empresa> search(@Param("query") String query);

}
