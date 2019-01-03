package com.earandap.vehiculos.domain;

import javax.persistence.*;

/**
 * Created by angel on 06/04/16.
 */
@Entity
@Table(name = "municipios")
@SequenceGenerator(sequenceName = "municipios_seq", name = "SEQ_MUNICIPIOS")
public class Municipios {

    @Id
    @Column(name = "municipios_id")
    @GeneratedValue(generator = "SEQ_MUNICIPIOS", strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "municipios_codigo")
    private String codigo;

    @Column(name = "municipios_nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

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

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Municipios)) return false;

        Municipios that = (Municipios) o;

        if (getId() != that.getId()) return false;
        if (getCodigo() != null ? !getCodigo().equals(that.getCodigo()) : that.getCodigo() != null) return false;
        if (getNombre() != null ? !getNombre().equals(that.getNombre()) : that.getNombre() != null) return false;
        return getDepartamento() != null ? getDepartamento().equals(that.getDepartamento()) : that.getDepartamento() == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getCodigo() != null ? getCodigo().hashCode() : 0);
        result = 31 * result + (getNombre() != null ? getNombre().hashCode() : 0);
        result = 31 * result + (getDepartamento() != null ? getDepartamento().hashCode() : 0);
        return result;
    }
}
