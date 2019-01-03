package com.earandap.vehiculos.domain.nomenclador;

import java.io.Serializable;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */
@Entity
@Table(name = "nomenclador")
@SequenceGenerator(sequenceName = "nomenclador_seq", name = "SEQ_NOMENCLADOR")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "nomenclador_tipo", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorOptions(force = true)
public class Nomenclador implements Serializable {

    @Id
    @Column(name = "nomenclador_id")
    @GeneratedValue(generator = "SEQ_NOMENCLADOR", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "nomenclador_codigo")
    private String codigo;

    @Column(name = "nomenclador_descripcion")
    private String descripcion;

    @Column(name = "nomenclador_tipo", insertable = false, updatable = false)
    protected String tipo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Nomenclador other = (Nomenclador) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
