package com.earandap.vehiculos.domain;

import com.earandap.vehiculos.domain.nomenclador.Cargo;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * @Autor Eduardo Aranda
 * @Version 0.1
 */

@Entity
@Table(name = "empleado")
@SequenceGenerator(sequenceName = "empleado_seq", name = "SEQ_EMPLEADO")
public class Empleado {

    @Id
    @Column(name = "empleado_id")
    @GeneratedValue(generator = "SEQ_EMPLEADO", strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    /*@OneToOne
    @JoinColumn(name="tercero_id")*/
    @OneToOne(mappedBy = "empleado")
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE)
    private Persona persona;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    //TODO
}
