package com.earandap.vehiculos.domain;

import javax.persistence.*;

/**
 * Created by Angel Luis on 11/6/2015.
 */
@Entity
@Table(name = "recurso")
@SequenceGenerator(sequenceName = "recurso_seq", name = "SEQ_RECURSO")
public class Recurso {

    @Id
    @Column(name = "recurso_id")
    @GeneratedValue(generator = "SEQ_RECURSO", strategy = GenerationType.AUTO)
    private long id;

   /* @ManyToOne
    @JoinColumn(name = "tiporecurso_id")
    private TipoRecurso tipoRecurso;*/

    @Column(name = "recurso_nombre")
    private String nombre;

    @Column(name = "recurso_codigo")
    private String codigo;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
