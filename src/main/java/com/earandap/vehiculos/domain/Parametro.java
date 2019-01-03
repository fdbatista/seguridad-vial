package com.earandap.vehiculos.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * @Autor felix.batista
 */
@Entity
@Table(name = "parametros")
@SequenceGenerator(sequenceName = "params_seq", name = "SEQ_PARAMS")
public class Parametro implements Serializable {

    @Id
    @Column(name = "param_id")
    @GeneratedValue(generator = "SEQ_PARAMS", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "param_nombre")
    private String nombreParametro;
    
    @Column(name = "param_valor")
    private String valorParametro;
    
    public Parametro() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public String getValorParametro() {
        return valorParametro;
    }

    public void setValorParametro(String valorParametro) {
        this.valorParametro = valorParametro;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
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
        final Parametro other = (Parametro) obj;
        return this.id == other.id;
    }
}
