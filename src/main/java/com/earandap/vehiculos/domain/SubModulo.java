package com.earandap.vehiculos.domain;

import javax.persistence.*;

/**
 * Created by Angel Luis on 11/9/2015.
 */
@Entity
@Table(name = "submodulo")
@SequenceGenerator(sequenceName = "submodulo_seq", name = "SEQ_SUBMODULO")
public class SubModulo {

    @Id
    @Column(name = "submodulo_id")
    @GeneratedValue(generator = "SEQ_SUBMODULO", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "submodulo_nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;

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

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }
}
