package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.annotations.Cascade;

/**
 * Created by Angel Luis on 11/6/2015.
 */
@Entity
@Table(name = "perfil")
@SequenceGenerator(sequenceName = "perfil_seq", name = "SEQ_PERFIL")
public class Perfil {

    @Id
    @Column(name = "perfil_id")
    @GeneratedValue(generator = "SEQ_PERFIL", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "perfil_nombre")
    private String nombre;

    @OneToMany(mappedBy = "id.perfil", fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private Set<PerfilRecurso> perfilRecursos = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "perfil", fetch = FetchType.EAGER)
    private Set<Usuario> usuarios = new LinkedHashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PerfilRecurso> getPerfilRecursos() {
        return perfilRecursos;
    }

    public void setPerfilRecursos(Set<PerfilRecurso> perfilRecursos) {
        this.perfilRecursos = perfilRecursos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil)) return false;

        Perfil perfil = (Perfil) o;

        if (id != perfil.id) return false;

        return true;
        //return !(perfilRecursos != null ? !perfilRecursos.equals(perfil.perfilRecursos) : perfil.perfilRecursos != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nombre.hashCode();
        result = 31 * result + (perfilRecursos != null ? perfilRecursos.hashCode() : 0);
        return result;
    }
}
