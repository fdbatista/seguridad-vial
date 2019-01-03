package com.earandap.vehiculos.domain;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Angel Luis on 11/9/2015.
 */
@Entity
@Table(name = "modulo")
@SequenceGenerator(sequenceName = "modulo_seq", name = "SEQ_MODULO")
public class Modulo {

    @Id
    @Column(name = "modulo_id")
    @GeneratedValue(generator = "SEQ_MODULO", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "modulo_nombre")
    private String nombre;

    @OneToMany(mappedBy = "modulo", fetch = FetchType.EAGER)
    private Set<SubModulo> subModulos = new LinkedHashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<SubModulo> getSubModulos() {
        return subModulos;
    }

    public void setSubModulos(Set<SubModulo> subModulos) {
        this.subModulos = subModulos;
    }
}
