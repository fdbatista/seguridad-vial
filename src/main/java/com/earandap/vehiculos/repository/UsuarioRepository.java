package com.earandap.vehiculos.repository;

import com.earandap.vehiculos.domain.Persona;
import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.domain.nomenclador.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Angel Luis on 11/9/2015.
 */
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

    Optional<Object> findOneByUsuario(String usuario);

    Optional<Object> findOneByPersona(Persona persona);

    List<Usuario> findAllByIdNot(long id);

    Usuario findByUsuario(String usuario);
    
    @Query("SELECT u FROM Usuario u WHERE u.perfil.id =:perfil_id")
    List<Usuario> findAdministradores(@Param("perfil_id") Long perfilId);

}
