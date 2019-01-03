package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Modulo;
import com.earandap.vehiculos.domain.Perfil;
import com.earandap.vehiculos.domain.PerfilRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Angel Luis on 11/13/2015.
 */
@Transactional
public interface PerfilRecursoRepository extends JpaRepository<PerfilRecurso,PerfilRecurso.Id> {
    @Query("SELECT pf FROM PerfilRecurso pf WHERE pf.id.perfil = :perfil AND pf.subModulo.modulo = :modulo")
    List<PerfilRecurso> findAllByModulo(@Param("perfil") Perfil perfil,@Param("modulo") Modulo modulo);

    @Query("SELECT CASE WHEN COUNT(pf) > 0 THEN true ELSE false END FROM PerfilRecurso pf WHERE pf.id.perfil.nombre = :perfil AND pf.id.recurso.codigo = :vista and pf.consultar = true")
    boolean tienePermisoConsultar(@Param("perfil") String perfil, @Param("vista") String vista);
}
