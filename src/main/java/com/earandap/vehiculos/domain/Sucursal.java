package com.earandap.vehiculos.domain;

import javax.persistence.*;

/**
 * Created by Angel Luis on 11/16/2015.
 */
@Entity
@Table(name = "sucursal")
@SequenceGenerator(sequenceName = "sucursal_seq", name = "SEQ_SUCURSAL")
public class Sucursal {

    @Id
    @Column(name = "sucursal_id")
    @GeneratedValue(generator = "SEQ_SUCURSAL", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "sucursal_nombre")
    private String nombre;

    @Column(name = "sucursal_inactivo")
    private boolean inactivo;

    @ManyToOne
    @JoinColumn(name = "tercero_id")
    private Empresa empresa;

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

    public boolean isInactivo() {
        return inactivo;
    }

    public void setInactivo(boolean inactivo) {
        this.inactivo = inactivo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
