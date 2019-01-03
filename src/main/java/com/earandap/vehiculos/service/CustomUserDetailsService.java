package com.earandap.vehiculos.service;


import com.earandap.vehiculos.domain.Usuario;
import com.earandap.vehiculos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{

   @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String ssoId)
            throws UsernameNotFoundException {
            Usuario user = usuarioRepository.findByUsuario(ssoId);

        if(user==null){
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return user;
    }


}
