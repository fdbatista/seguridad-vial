package com.earandap.vehiculos.domain;

import javax.persistence.*;

/**
 * Created by angel on 06/04/16.
 */
@Entity
@Table(name = "departamento")
@SequenceGenerator(sequenceName = "departamento_seq", name = "SEQ_DEPARTAMENTO")
public class Departamento {

    @Id
    @Column(name = "departamento_id")
    @GeneratedValue(generator = "SEQ_DEPARTAMENTO", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "departamento_codigo")
    private String codigo;

    @Column(name = "departamento_nombre")
    private String nombre;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
