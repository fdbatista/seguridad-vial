package com.earandap.vehiculos.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Angel Luis on 11/4/2015.
 */
@Entity
@Table(name = "usuario")
@SequenceGenerator(sequenceName = "usuario_seq", name = "SEQ_USUARIO")
public class Usuario implements UserDetails{

    @Id
    @Column(name = "usuario_id")
    @GeneratedValue(generator = "SEQ_USUARIO", strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Pattern(regexp = "^[a-z0-9]*$")
    @Size(min = 1, max = 50)
    @Column(name = "usuario_usuario", length = 50, unique = true, nullable = false)
    private String usuario;

    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "usuario_contrasena", length = 1000)
    private String contrasena;

    @Column(name = "usuario_inactivo", nullable = false)
    private boolean inactivo = false;

    @Column(name = "usuario_fechacreacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @OneToOne
    @JoinColumn(name="persona_id")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isInactivo() {
        return inactivo;
    }

    public void setInactivo(boolean inactivo) {
        this.inactivo = inactivo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(perfil!= null && perfil.getNombre() != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(perfil.getNombre()));
            return authorities;
        }
        return null;
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !inactivo;
    }

    public Usuario() {
    }
}
